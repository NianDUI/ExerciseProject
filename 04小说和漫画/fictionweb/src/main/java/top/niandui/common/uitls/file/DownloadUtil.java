package top.niandui.common.uitls.file;

import top.niandui.common.expection.ReStateException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @Title: DownloadUtils.java
 * @description: 下载工具
 * @time: 2020/5/18 14:26
 * @author: liyongda
 * @version: 1.0
 */
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
     * @param filePath 文件路径
     * @param response HttpServletResponse对象
     */
    public static void downloadFile(String filePath, HttpServletResponse response) {
        // 文件
        File file = new File(filePath);
        // 取得文件名。
        String filename = file.getName();
        try (
                FileInputStream fis = new FileInputStream(file);
                OutputStream os = response.getOutputStream()
        ) {
            // 下载设置
            response.reset();
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            int len;
            byte[] b = new byte[1024];
            while ((len = fis.read(b)) != -1) {
                os.write(b, 0, len);
            }
            os.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ReStateException("下载资源名称转码异常");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new ReStateException("下载资源不存在");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReStateException("下载资源流异常");
        }
    }
}
