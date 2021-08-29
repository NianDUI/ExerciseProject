package top.niandui.utils;

import org.springframework.util.DigestUtils;
import top.niandui.model.Papers;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

import static top.niandui.common.uitls.DateUtil.dateFormat;
import static top.niandui.common.uitls.file.FileUtil.unitConvert;
import static top.niandui.config.PublicBean.configInfo;
import static top.niandui.config.PublicBean.redisUtil;
import static top.niandui.config.PublicConstant.*;

/**
 * 路径工具
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/9/17 16:29
 */
public class PathUtil {
    // 文件路径长度
    public static int filePathLength = configInfo.getFilePath().length();

    /**
     * 获取路径
     *
     * @param request 请求对象
     * @param endStr  路径结束符
     * @return [请求路径(md5), 对应具体路径]
     * @throws Exception
     */
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
        path = (String) redisUtil.hGet(PATH_MAPPING_M_P, path);
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

    /**
     * 获取路径md5
     *
     * @param path 路径
     * @return 路径md5
     */
    public static String getMd5(String path) {
        path = path.replace("\\", "/");
        if (path.startsWith(configInfo.getFilePath())) {
            path = path.substring(filePathLength);
        }
        if (path.length() != 0) {
            String md5 = (String) redisUtil.hGet(PATH_MAPPING_P_M, path);
            if (md5 == null) {
                md5 = DigestUtils.md5DigestAsHex(path.getBytes());
                redisUtil.hSet(PATH_MAPPING_M_P, md5, path, DAY_SECOND);
                redisUtil.hSet(PATH_MAPPING_P_M, path, md5, DAY_SECOND);
            }
            path = md5;
        }
        return path;
    }

    /**
     * 获取 Papers 对象
     *
     * @param file File文件或文件夹对象
     * @return Papers文件包装对象
     */
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
