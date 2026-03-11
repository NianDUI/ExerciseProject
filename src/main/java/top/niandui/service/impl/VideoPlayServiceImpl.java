package top.niandui.service.impl;

import jakarta.annotation.PreDestroy;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import top.niandui.config.ConfigInfo;
import top.niandui.model.vo.VideoPlaySourceVO;
import top.niandui.service.IVideoPlayService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static top.niandui.utils.PathUtil.getPath;

@Slf4j
@Service
public class VideoPlayServiceImpl implements IVideoPlayService {
    private static final String HLS_INDEX = "index.m3u8";
    private static final int MAX_LOG_BYTES = 16 * 1024;

    private final Map<String, String> taskStatusMap = new ConcurrentHashMap<>();
    private final Map<String, String> taskErrorMap = new ConcurrentHashMap<>();
    private final Map<String, Object> taskLockMap = new ConcurrentHashMap<>();
    private volatile ExecutorService transcodeExecutor;

    @Autowired
    private ConfigInfo configInfo;

    @Override
    public VideoPlaySourceVO resolveSource(HttpServletRequest request) throws Exception {
        String[] paths = getPath(request, "source");
        return resolveSourceInternal(paths[1], "/api/file/download/" + paths[0]);
    }

    @Override
    public VideoPlaySourceVO resolveSourceByPath(String path) throws Exception {
        String safePath = normalizeRelativePath(path);
        return resolveSourceInternal(safePath, "/api/file/downloadByPath?path=" + java.net.URLEncoder.encode(safePath, StandardCharsets.UTF_8));
    }

    private VideoPlaySourceVO resolveSourceInternal(String relativePath, String directSourceUrl) throws Exception {
        File sourceFile = new File(getPath(relativePath));
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            throw new RuntimeException("视频文件不存在");
        }
        VideoPlaySourceVO source = new VideoPlaySourceVO();
        source.setTitle(sourceFile.getName());
        if (isDirectPlayFile(sourceFile.getName())) {
            source.setStatus("ready");
            source.setSourceUrl(directSourceUrl);
            source.setMimeType(detectMimeType(sourceFile.getName()));
            source.setTranscoded(Boolean.FALSE);
            source.setMessage("视频已就绪");
            return source;
        }
        String cacheKey = buildCacheKey(sourceFile);
        Path cacheDir = getVideoCacheRoot().toPath().resolve(cacheKey);
        Path playlist = cacheDir.resolve(HLS_INDEX);
        if (Files.exists(playlist)) {
            touchPath(cacheDir);
            source.setStatus("ready");
            source.setSourceUrl("/api/file/video/hls/" + cacheKey + "/" + HLS_INDEX);
            source.setMimeType("application/x-mpegURL");
            source.setTranscoded(Boolean.TRUE);
            source.setMessage("视频已就绪");
            return source;
        }
        String status = taskStatusMap.get(cacheKey);
        if ("failed".equals(status)) {
            source.setStatus("failed");
            source.setMimeType("application/x-mpegURL");
            source.setTranscoded(Boolean.TRUE);
            source.setMessage(taskErrorMap.getOrDefault(cacheKey, "视频转码失败"));
            return source;
        }
        submitTranscodeIfNeeded(sourceFile.toPath(), cacheKey, cacheDir);
        source.setStatus("processing");
        source.setMimeType("application/x-mpegURL");
        source.setTranscoded(Boolean.TRUE);
        source.setMessage("视频转码中，请稍候重试");
        return source;
    }

    @Override
    public void streamHls(String cacheKey, String fileName, HttpServletResponse response) throws Exception {
        if (!StringUtils.hasText(cacheKey) || !StringUtils.hasText(fileName)) {
            throw new RuntimeException("播放资源参数错误");
        }
        File target = new File(getVideoCacheRoot(), cacheKey + File.separator + fileName).getCanonicalFile();
        File cacheRoot = getVideoCacheRoot().getCanonicalFile();
        if (!target.getPath().startsWith(cacheRoot.getPath())) {
            throw new RuntimeException("非法播放路径");
        }
        if (!target.exists() || !target.isFile()) {
            throw new RuntimeException("播放资源不存在");
        }
        touchPath(target.toPath().getParent());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(resolveHlsContentType(fileName));
        response.setHeader("Cache-Control", "public, max-age=3600");
        try (InputStream inputStream = Files.newInputStream(target.toPath(), StandardOpenOption.READ);
             ServletOutputStream os = response.getOutputStream()) {
            inputStream.transferTo(os);
            os.flush();
        }
    }

    public void cleanupExpiredCache() {
        File root = getVideoCacheRoot();
        if (!root.exists() || !root.isDirectory()) {
            return;
        }
        long expireHours = Math.max(1, configInfo.getVideoCacheExpireHours() == null ? 72 : configInfo.getVideoCacheExpireHours());
        long expireBefore = System.currentTimeMillis() - expireHours * 60L * 60L * 1000L;
        File[] children = root.listFiles(File::isDirectory);
        if (children == null) {
            return;
        }
        for (File child : children) {
            if (child.lastModified() >= expireBefore) {
                continue;
            }
            String cacheKey = child.getName();
            if ("processing".equals(taskStatusMap.get(cacheKey))) {
                continue;
            }
            try {
                deleteDirectory(child.toPath());
                taskStatusMap.remove(cacheKey);
                taskErrorMap.remove(cacheKey);
                taskLockMap.remove(cacheKey);
                log.info("清理过期转码缓存：{}", child.getAbsolutePath());
            } catch (Exception e) {
                log.warn("清理转码缓存失败：{}", child.getAbsolutePath(), e);
            }
        }
    }

    private void submitTranscodeIfNeeded(Path sourceFile, String cacheKey, Path cacheDir) throws IOException {
        taskStatusMap.putIfAbsent(cacheKey, "pending");
        Object lock = taskLockMap.computeIfAbsent(cacheKey, key -> new Object());
        synchronized (lock) {
            String currentStatus = taskStatusMap.get(cacheKey);
            Path playlist = cacheDir.resolve(HLS_INDEX);
            if (Files.exists(playlist)) {
                taskStatusMap.put(cacheKey, "ready");
                taskErrorMap.remove(cacheKey);
                return;
            }
            if ("processing".equals(currentStatus)) {
                return;
            }
            Files.createDirectories(cacheDir);
            taskStatusMap.put(cacheKey, "processing");
            taskErrorMap.remove(cacheKey);
            getTranscodeExecutor().submit(() -> runTranscode(sourceFile, cacheKey, cacheDir));
        }
    }

    private void runTranscode(Path sourceFile, String cacheKey, Path cacheDir) {
        try {
            transcodeToHls(sourceFile, cacheDir);
            taskStatusMap.put(cacheKey, "ready");
            taskErrorMap.remove(cacheKey);
            touchPath(cacheDir);
            log.info("视频转码完成：{}", sourceFile);
        } catch (Exception e) {
            taskStatusMap.put(cacheKey, "failed");
            taskErrorMap.put(cacheKey, e.getMessage());
            try {
                deleteDirectory(cacheDir);
            } catch (Exception ex) {
                log.warn("清理失败转码目录异常：{}", cacheDir, ex);
            }
            log.error("视频转码失败：{}", sourceFile, e);
        }
    }

    private void transcodeToHls(Path sourceFile, Path cacheDir) throws Exception {
        List<String> command = new ArrayList<>();
        command.add(configInfo.getFfmpegCommand());
        command.add("-y");
        command.add("-i");
        command.add(sourceFile.toAbsolutePath().toString());
        command.add("-c:v");
        command.add("libx264");
        command.add("-c:a");
        command.add("aac");
        command.add("-preset");
        command.add("veryfast");
        command.add("-crf");
        command.add("23");
        command.add("-f");
        command.add("hls");
        command.add("-hls_time");
        command.add("6");
        command.add("-hls_playlist_type");
        command.add("vod");
        command.add("-hls_segment_filename");
        command.add(cacheDir.resolve("segment_%05d.ts").toAbsolutePath().toString());
        command.add(cacheDir.resolve(HLS_INDEX).toAbsolutePath().toString());
        Process process;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            process = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException("视频转码失败：未找到 ffmpeg，请配置 config-info.ffmpeg-command", e);
        }
        String output;
        try (InputStream inputStream = process.getInputStream()) {
            output = readProcessOutput(inputStream);
        }
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            log.error("ffmpeg 转码失败，exitCode={}，output={}", exitCode, output);
            throw new RuntimeException("视频转码失败，请检查视频格式或 ffmpeg 配置");
        }
    }

    private String readProcessOutput(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            if (outputStream.size() < MAX_LOG_BYTES) {
                int writable = Math.min(read, MAX_LOG_BYTES - outputStream.size());
                outputStream.write(buffer, 0, writable);
            }
        }
        return outputStream.toString(StandardCharsets.UTF_8);
    }

    private ExecutorService getTranscodeExecutor() {
        if (transcodeExecutor == null) {
            synchronized (this) {
                if (transcodeExecutor == null) {
                    int size = Math.max(1, configInfo.getVideoTranscodeThreads() == null ? 2 : configInfo.getVideoTranscodeThreads());
                    AtomicInteger counter = new AtomicInteger(1);
                    ThreadFactory factory = runnable -> {
                        Thread thread = new Thread(runnable);
                        thread.setName("video-transcode-" + counter.getAndIncrement());
                        thread.setDaemon(true);
                        return thread;
                    };
                    transcodeExecutor = Executors.newFixedThreadPool(size, factory);
                }
            }
        }
        return transcodeExecutor;
    }

    private File getVideoCacheRoot() {
        String configured = configInfo.getVideoCachePath();
        if (!StringUtils.hasText(configured)) {
            configured = ".transcode";
        }
        File configuredFile = new File(configured);
        if (configuredFile.isAbsolute()) {
            return configuredFile;
        }
        return new File(configInfo.getFilePath(), configured);
    }

    private String buildCacheKey(File sourceFile) {
        String raw = sourceFile.getAbsolutePath() + "|" + sourceFile.length() + "|" + sourceFile.lastModified();
        return DigestUtils.md5DigestAsHex(raw.getBytes(StandardCharsets.UTF_8));
    }

    private String normalizeRelativePath(String path) {
        if (!StringUtils.hasText(path)) {
            throw new RuntimeException("文件路径不能为空");
        }
        String safePath = path.replace("\\", "/").replace("..", ".");
        if (!safePath.startsWith("/")) {
            safePath = "/" + safePath;
        }
        return safePath;
    }

    private boolean isDirectPlayFile(String name) {
        String lower = String.valueOf(name).toLowerCase(Locale.ROOT);
        return lower.endsWith(".mp4") || lower.endsWith(".webm");
    }

    private String detectMimeType(String name) {
        String lower = String.valueOf(name).toLowerCase(Locale.ROOT);
        if (lower.endsWith(".webm")) {
            return "video/webm";
        }
        return "video/mp4";
    }

    private String resolveHlsContentType(String fileName) throws IOException {
        String lower = fileName.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".m3u8")) {
            return "application/vnd.apple.mpegurl";
        }
        if (lower.endsWith(".ts")) {
            return "video/mp2t";
        }
        String probed = Files.probeContentType(Path.of(fileName));
        return StringUtils.hasText(probed) ? probed : "application/octet-stream";
    }

    private void touchPath(Path path) {
        if (path == null || !Files.exists(path)) {
            return;
        }
        try {
            Files.setLastModifiedTime(path, FileTime.from(Instant.now()));
        } catch (Exception e) {
            log.debug("更新缓存访问时间失败：{}", path, e);
        }
    }

    private void deleteDirectory(Path path) throws IOException {
        if (path == null || !Files.exists(path)) {
            return;
        }
        try (var stream = Files.walk(path)) {
            stream.sorted((a, b) -> b.getNameCount() - a.getNameCount()).forEach(item -> {
                try {
                    Files.deleteIfExists(item);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e) {
            if (e.getCause() instanceof IOException ioException) {
                throw ioException;
            }
            throw e;
        }
    }

    @PreDestroy
    public void shutdownExecutor() {
        if (transcodeExecutor != null) {
            transcodeExecutor.shutdownNow();
        }
    }
}
