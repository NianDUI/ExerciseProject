package top.niandui.service.impl;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.niandui.config.ConfigInfo;
import top.niandui.model.Papers;
import top.niandui.service.IFileService;
import top.niandui.utils.PathUtil;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

import static top.niandui.common.uitls.file.DownloadUtil.downloadFile;
import static top.niandui.utils.PathUtil.getPapers;
import static top.niandui.utils.PathUtil.getPath;

/**
 * @Title: FileServiceImpl.java
 * @description: 文件Service实现类
 * @time: 2020/9/9 11:11
 * @author: liyongda
 * @version: 1.0
 */
@Slf4j
@Service
public class FileServiceImpl implements IFileService {
    @Autowired
    private ConfigInfo configInfo;

    @Override
    public void read(String path, HttpServletResponse response) throws Exception {
        if (StringUtils.hasText(path)) {
            // 获取请求目录文件对象
            File file = new File(configInfo.getFilePath(), path.replace("..", "."));
            if (file.exists() && file.isFile()) {
                // 文件存在并且是文件
                ServletOutputStream os = response.getOutputStream();
                Files.copy(file.toPath(), os);
                os.close();
                log.info("读取文件：" + file.getAbsolutePath());
            }
        }
    }

    @Override
    public List<Papers> list(HttpServletRequest request) throws Exception {
        return list(getPath(request, "list")[1]);
    }

    @Override
    public List<Papers> list(String path) throws Exception {
        List<Papers> list;
        // 获取请求目录文件夹对象
        File file = new File(configInfo.getFilePath() + path);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            // 返回数据列表
            list = new ArrayList<>(files.length + 1);
            if (!"/".equals(path)) {
                // 当前目录不是根目录，添加父目录
                Papers papers = getPapers(file.getParentFile());
                papers.setName("..");
                papers.setIsDir(true);
                papers.setIsFile(false);
                papers.setIsExists(true);
                list.add(papers);
            }
            // 处理添加目录下文件夹
            Arrays.stream(files).filter(File::isDirectory).sorted(Comparator.comparing(File::getName)).map(PathUtil::getPapers).forEach(list::add);
            // 处理添加目录下文件
            Arrays.stream(files).filter(File::isFile).sorted(Comparator.comparing(File::getName)).map(PathUtil::getPapers).forEach(list::add);
            log.info("读取目录：" + file.getAbsolutePath());
        } else {
            list = Collections.emptyList();
        }
        return list;
    }

    @Override
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取请求路径
        String filePath = configInfo.getFilePath() + getPath(request, "download")[1];
        log.info("下载文件：" + filePath);
        downloadFile(request, response, filePath);
    }
}
