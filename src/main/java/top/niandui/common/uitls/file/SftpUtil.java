package top.niandui.common.uitls.file;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Iterator;

/**
 * Sftp工具
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/5/7 13:55
 */
@Slf4j
public class SftpUtil {
    // 分隔符字符
    private static final char SEPARATOR_CHAR = '/';

    private SftpUtil() {
    }

    /**
     * 获取sftp通道对象
     *
     * @param host     主机地址
     * @param port     端口号
     * @param username 用户名
     * @param password 密码
     * @return sftp通道对象
     */
    public static ChannelSftp getChannelSftp(String host, int port, String username, String password) {
        try {
            // 创建JSch对象
            JSch jsch = new JSch();
            // 通过用户名、主机、端口号获取会话对象
            Session session = jsch.getSession(username, host, port);
            // 设置连接面
            session.setPassword(password);
            // 不验证主机密钥
            session.setConfig("StrictHostKeyChecking", "no");
            // 建立会话
            session.connect();
            // 打开sftp通道
            Channel channel = session.openChannel("sftp");
            // 建立通道
            channel.connect();
            return (ChannelSftp) channel;
        } catch (Exception e) {
            throw new RuntimeException("sftp连接错误", e);
        }
    }

    /**
     * 关闭sftp通道
     *
     * @param sftp sftp通道对象
     */
    public static void closeChannelSftp(ChannelSftp sftp) {
        try {
            sftp.disconnect();
            sftp.getSession().disconnect();
        } catch (Exception e) {
            log.error("sftp连接关闭失败", e);
        }
    }

    /**
     * 从sftp服务器上下载指定文件或文件夹，到本地指定的目录
     *
     * @param sftp       sftp通道对象
     * @param remotePath 远程路径
     * @param localPath  本地路径
     * @throws Exception
     */
    public static void download(ChannelSftp sftp, String remotePath, String localPath) throws Exception {
        // sftp为null、或sftp是关闭的
        if (sftp == null || sftp.isClosed()) {
            throw new RuntimeException("sftp文件下载失败");
        }
        // 本地存放文件夹
        File localDir = new File(localPath);
        if (!localDir.exists()) {
            localDir.mkdirs();
        }
        localPath = localDir.getAbsolutePath();
        log.info("sftp d {} > {}", remotePath, localPath);
        // 查询文件列表
        Iterator<ChannelSftp.LsEntry> it = sftp.ls(remotePath).iterator();
        // 遍历
        while (it.hasNext()) {
            ChannelSftp.LsEntry entry = it.next();
            if (".".equals(entry.getFilename()) || "..".equals(entry.getFilename())) {
                // 本页和上页跳过
                continue;
            }
            String path = SEPARATOR_CHAR + entry.getFilename();
            // 判断是否为为文件夹
            if (entry.getAttrs().isDir()) {
                // 该项为文件夹递归调用
                download(sftp, remotePath + path, localPath + path);
            } else {
                // 为文件直接下载
                String remoteFilePath = remotePath;
                if (!remotePath.endsWith(entry.getFilename())) {
                    // 远程文件路径不以文件名结尾
                    remoteFilePath += path;
                }
                // 下载文件
                sftp.get(remoteFilePath, localPath + path);
            }
        }
    }

    /**
     * 从本地指定的文件或文件夹上传到sftp服务器上指目录
     *
     * @param sftp       sftp通道对象
     * @param localPath  本地路径
     * @param remotePath 远程路径
     * @throws Exception
     */
    public static void upload(ChannelSftp sftp, String localPath, String remotePath) throws Exception {
        log.info("sftp u {} > {}", localPath, remotePath);
        upload(sftp, new File(localPath), remotePath);
    }

    /**
     * 从本地指定的文件或文件夹上传到sftp服务器上指目录
     *
     * @param sftp       sftp通道对象
     * @param localFile  本地文件对象
     * @param remotePath 远程路径
     * @throws Exception
     */
    private static void upload(ChannelSftp sftp, File localFile, String remotePath) throws Exception {
        // 判断本地文件
        if (localFile == null || !localFile.exists()) {
            throw new RuntimeException("本地文件不存在");
        }
        // sftp为null、或sftp是关闭的
        if (sftp == null || sftp.isClosed()) {
            throw new RuntimeException("sftp文件上传失败");
        }
        // 判断远程路径是否为文件夹
        if (!isDir(sftp, remotePath)) {
            // 远程路径不存在，创建
            sftp.mkdir(remotePath);
        }
        // 远程文件、文件夹路径
        String remoteFilePath = remotePath + SEPARATOR_CHAR + localFile.getName();
        if (localFile.isDirectory()) {
            // 是文件夹。本页和上页跳过
            File[] files = localFile.listFiles((dir, name1) -> !".".equals(name1) && !"..".equals(name1));
            for (File file : files) {
                // 递归上传
                upload(sftp, file, remoteFilePath);
            }
        } else if (localFile.isFile()) {
            // 是文件。上传文件
            sftp.put(localFile.getAbsolutePath(), remoteFilePath);
        }
    }

    /**
     * 判断远程路径是否为为文件夹
     *
     * @param sftp       sftp通道对象
     * @param remotePath 远程路径
     * @return true 是、false 否
     */
    public static boolean isDir(ChannelSftp sftp, String remotePath) {
        boolean isDir;
        try {
            SftpATTRS stat = sftp.stat(remotePath);
            isDir = stat.isDir();
        } catch (SftpException e) {
            isDir = false;
        }
        return isDir;
    }

}
