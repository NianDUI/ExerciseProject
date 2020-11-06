package top.niandui.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import top.niandui.common.uitls.redis.RedisUtil;
import top.niandui.config.ConfigInfo;
import top.niandui.model.Papers;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

import static top.niandui.common.uitls.DateUtil.dateFormat;
import static top.niandui.common.uitls.file.FileUtil.unitConvert;

/**
 * @Title: PathUtil.java
 * @description: 路径工具
 * @time: 2020/9/17 16:29
 * @author: liyongda
 * @version: 1.0
 */
@Component
public class PathUtil {
    // 路径映射
    public static final String PATH_MAPPING_P_M = "path_mapping:p_m";
    public static final String PATH_MAPPING_M_P = "path_mapping:m_p";
    // 路径保存时间
    public static final long PATH_SAVE_TIME = 60 * 60 * 24;
    public static ConfigInfo configInfo;
    // 文件路径长度
    public static int filePathLength;
    public static RedisUtil redisUtil;

    @Autowired
    public PathUtil(ConfigInfo configInfo, RedisUtil redisUtil) {
        PathUtil.configInfo = configInfo;
        PathUtil.redisUtil = redisUtil;
        filePathLength = configInfo.getFilePath().length();
    }

    // 获取路径
    public static String[] getPath(HttpServletRequest request, String endStr) throws Exception {
        String path = request.getRequestURI().trim();
        path = path.substring(path.indexOf(endStr) + endStr.length());
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String[] paths = new String[2];
        paths[0] = path;
        if (path.length() == 0) {
            paths[1] = "/";
            return paths;
        }
        path = (String) redisUtil.hget(PATH_MAPPING_M_P, path);
        if (path == null) {
            paths[1] = "/";
            return paths;
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        paths[1] = path;
        return paths;
    }

    // 获取路径md5
    public static String getMd5(String path) {
        path = path.replace("\\", "/");
        if (path.startsWith(configInfo.getFilePath())) {
            path = path.substring(filePathLength);
        }
        if (path.length() != 0) {
            String md5 = (String) redisUtil.hget(PATH_MAPPING_P_M, path);
            if (md5 == null) {
                md5 = DigestUtils.md5DigestAsHex(path.getBytes());
                redisUtil.hset(PATH_MAPPING_M_P, md5, path, PATH_SAVE_TIME);
                redisUtil.hset(PATH_MAPPING_P_M, path, md5, PATH_SAVE_TIME);
            }
            path = md5;
        }
        return path;
    }

//    @Autowired
//    public void setRedisUtil(RedisUtil redisUtil) {
//        PathUtil.redisUtil = redisUtil;
//    }

    // 获取 Papers 对象
    public static Papers getPapers(File file) {
        Papers papers = new Papers();
        papers.setFile(file);
        papers.setIsExists(file.exists());
        if (file != null && papers.getIsExists()) {
            papers.setName(file.getName());
            papers.setPath(getMd5(file.getAbsolutePath()));
            String[] sizeUnit = unitConvert(file.length());
            papers.setSize(Double.valueOf(sizeUnit[0]));
            papers.setUnit(sizeUnit[1]);
            papers.setLastModified(dateFormat(file.lastModified()));
            papers.setIsDir(file.isDirectory());
            papers.setIsFile(file.isFile());
        }
        return papers;
    }
}
