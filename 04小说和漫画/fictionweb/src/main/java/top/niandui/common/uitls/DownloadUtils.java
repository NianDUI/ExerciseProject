package top.niandui.common.uitls;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @Title: DownloadUtils.java
 * @description: 下载工具
 * @time: 2020/5/18 14:26
 * @author: liyongda
 * @version: 1.0
 */
public class DownloadUtils {
    private DownloadUtils() {
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
}
