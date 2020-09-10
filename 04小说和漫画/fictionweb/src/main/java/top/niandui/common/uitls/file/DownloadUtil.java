package top.niandui.common.uitls.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;

/**
 * @Title: DownloadUtils.java
 * @description: 下载工具
 * @time: 2020/5/18 14:26
 * @author: liyongda
 * @version: 1.0
 */
@Slf4j
public class DownloadUtil {
    private DownloadUtil() {
    }

    /**
     * 获取下载输出流
     *
     * @param response 请求响应对象
     * @param fileName 下载文件名称
     * @return 下载输出流对象
     * @throws IOException
     */
    public static OutputStream getDownloadOS(HttpServletResponse response,
                                             String fileName) throws IOException {
        response.reset();
        response.setHeader("Accept-Ranges", "bytes");
//        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        return response.getOutputStream();
    }

    /**
     * 下载文件
     *
     * @param response HttpServletResponse对象
     * @param filePath 文件路径
     */
    public static void downloadFile(HttpServletResponse response, String filePath) {
        // 文件
        File file = new File(filePath);
        // 取得文件名。
        String filename = file.getName();
        try (OutputStream os = getDownloadOS(response, filename)) {
            // 下载设置
            response.setHeader("Content-Length", String.valueOf(file.length()));
            Files.copy(file.toPath(), os);
        } catch (UnsupportedEncodingException e) {
            log.error(e.toString());
            throw new RuntimeException("下载资源名称转码异常");
        } catch (FileNotFoundException e) {
            log.error(e.toString());
            throw new RuntimeException("下载资源不存在");
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException("下载资源流异常");
        }
    }

    /**
     * 下载文件(支持断点续传)
     *
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param filePath 文件路径
     */
    public static void downloadFile(HttpServletRequest request, HttpServletResponse response, String filePath) {
        // 文件
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new RuntimeException("文件不存在：" + filePath);
        }
        // 取得文件名。
        String filename = file.getName();
        try (
                InputStream is = new FileInputStream(file);
                OutputStream os = getDownloadOS(response, filename)
        ) {
            long length = file.length(), start = 0, end = length - 1;
            String range;
            // 若客户端传来Range，说明之前下载了一部分，设置206状态(SC_PARTIAL_CONTENT)
            if ((range = request.getHeader("Range")) != null) {
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                String[] strRange = range.split("=")[1].split("-");
                start = Long.parseLong(strRange[0].trim());
                if (strRange.length == 2) {
                    end = Long.parseLong(strRange[1].trim());
                }
                String contentRange = new StringBuffer("bytes ").append(start).append("-").append(end)
                        .append("/").append(length).toString();
                response.setHeader("Content-Range", contentRange);
            }
            response.setHeader("Content-Length", String.valueOf(end - start + 1));
            long num = StreamUtils.copyRange(is, os, start, end);
            response.setHeader("Content-Length", String.valueOf(num));
        } catch (Exception e) {
            if (e instanceof ClientAbortException) {
                // 浏览器点击取消
                log.info("下载取消：" + filePath);
            } else {
                log.error(e.toString());
                throw new RuntimeException("下载失败：" + filePath);
            }
        }
    }
}
