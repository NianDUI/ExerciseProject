package top.niandui.common.uitls.file;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

/**
 * @Title: FTPUtil.java
 * @description: FTP工具
 * @time: 2020/6/4 14:15
 * @author: liyongda
 * @version: 1.0
 */
public class FTPUtil {

    /**
     * 获取FTPClient对象
     *
     * @param hostname FTP主机地址
     * @param port     FTP主机端口号
     * @param username FTP主机登录用户名
     * @param password FTP主机登录密码
     * @return FTPClient对象
     */
    public static FTPClient getFTPClient(String hostname, int port, String username, String password) {
        try {
            FTPClient ftp = new FTPClient();
            // 设置ftp字符集
            ftp.setControlEncoding("UTF-8");
            // 连接ftp(当前项目所部署的服务器和ftp服务器之间可以相互通讯，表示连接成功)
            ftp.connect(hostname, port);
            // 输入账号和密码进行登录:匿名用户anonymous
            ftp.login(username, password);
            // 接受状态码(如果成功，返回230，如果失败返回503)
            int replyCode = ftp.getReplyCode();
            // 设置文件传输模式为二进制，可以保证传输的内容不会被改变
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            // 根据状态码检测ftp的连接，调用isPositiveCompletion(reply)：成功true，否则false
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                throw new RuntimeException("FTP服务器连接失败");
            }
            return ftp;
        } catch (SocketException e) {
            e.printStackTrace();
            throw new RuntimeException("FTP的IP地址可能错误");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("FTP的端口错误");
        }
    }

    /**
     * 关闭FTPClient
     *
     * @param ftp FTPClient对象
     * @return 是否成功
     */
    public static boolean closeFTP(FTPClient ftp) {
        if (ftp == null) {
            return false;
        }
        try {
            ftp.logout();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ftp.isConnected()) {
                    ftp.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("FTP关闭失败");
            }
        }
        return false;
    }

    /**
     * 从FTP服务器上下载指定文件或文件夹，到本地指定的目录
     *
     * @param ftp        FTPClient对象
     * @param sourcePath FTP文件路径
     * @param targetPath 本地存储路径
     * @throws Exception
     */
    public static void download(FTPClient ftp, String sourcePath, String targetPath) throws Exception {
        if (ftp == null) {
            throw new RuntimeException("FTP文件下载失败");
        }
        File targetFile = new File(targetPath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        targetPath = targetFile.getAbsolutePath();
        FTPFile[] ftpFiles = ftp.listFiles(sourcePath);
        for (FTPFile ftpFile : ftpFiles) {
            String path = "/" + ftpFile.getName();
            if (ftpFile.isDirectory()) {
                download(ftp, sourcePath + path, targetPath + path);
            } else {
                String sourceFilePath = sourcePath;
                if (!sourcePath.endsWith(ftpFile.getName())) {
                    sourceFilePath += path;
                }
                String targetFilePath = new String((targetPath + path).getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
                try (OutputStream os = new FileOutputStream(targetFilePath)) {
                    if (!ftp.retrieveFile(sourceFilePath, os)) {
                        throw new RuntimeException("FTP文件下载失败");
                    }
                }
            }
        }
    }
}
