package top.niandui.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.niandui.config.ConfigInfo;
import top.niandui.model.Papers;
import top.niandui.service.IFileService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Holder;
import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        if (!StringUtils.isEmpty(path)) {
            File file = new File(configInfo.getFilePath(), path.replace("..", "."));
            if (file.exists() && file.isFile()) {
                ServletOutputStream os = response.getOutputStream();
                Files.copy(file.toPath(), os);
                os.close();
                log.info("读取文件：" + file.getAbsolutePath());
            }
        }
    }

    @Override
    public List<Papers> list(HttpServletRequest request) throws Exception {
        return list(request, null);
    }

    @Override
    public List<Papers> list(HttpServletRequest request, Holder<String> pathHolder) throws Exception {
        String path = request.getRequestURI();
        path = URLDecoder.decode(path, "UTF-8").trim();
        path = path.substring(path.indexOf("list") + 4).replace("..", ".");
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        if (pathHolder != null) {
            pathHolder.value = path;
        }
        List<Papers> list;
        File file = new File(configInfo.getFilePath() + path);
        if (file.exists() && file.isDirectory()) {
            Papers.filePathLength = configInfo.getFilePath().length();
            File[] files = file.listFiles();
            list = new ArrayList<>(files.length + 1);
            if (!"/".equals(path)) {
                Papers papers = new Papers(file.getParentFile());
                papers.setName("..");
                papers.setIsDir(true);
                papers.setIsFile(false);
                papers.setIsExists(true);
                list.add(papers);
            }
            Arrays.stream(files).filter(File::isDirectory).forEach(f -> list.add(new Papers(f)));
            Arrays.stream(files).filter(File::isFile).forEach(f -> list.add(new Papers(f)));
            log.info("读取目录：" + file.getAbsolutePath());
        } else {
            list = Collections.emptyList();
        }
        return list;
    }
}
