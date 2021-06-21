package top.niandui.common.uitls.file;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

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

    static {
        // 设置当前日志打印机级别
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger(SftpUtil.class);
        logger.setLevel(Level.DEBUG);
    }

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
        log.debug("sftp download {} > {}", remotePath, localPath);
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
        log.debug("sftp upload {} > {}", localPath, remotePath);
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
        // 创建远程路径
        mkdirRP(sftp, remotePath);
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
     * 在sftp服务器上创建远程路径
     * </br>支持多层，远端路径分隔符默认为"/"
     *
     * @param sftp       sftp通道对象
     * @param remotePath 远程路径
     * @throws Exception
     */
    public static void mkdirRP(ChannelSftp sftp, String remotePath) throws SftpException {
        log.debug("sftp mkdir {}", remotePath);
        // ["", "home"] 或 [".", "home"]
        String[] rps = remotePath.split("/");
        for (int i = 0; i < rps.length; i++) {
            String path = rps[i];
            if (i == 0 && "".equals(path)) {
                path = "/";
            } else if (!".".equals(path) && !"..".equals(path)) {
                if (!isDir(sftp, path)) {
                    // 目录不存在创建目录
                    sftp.mkdir(path);
                }
            }
            // 进入路径
            sftp.cd(path);
        }
    }


    /**
     * 从sftp服务器上删除指定文件或文件夹
     *
     * @param sftp       sftp通道对象
     * @param remotePath 远程路径
     * @param rmThisDir  当远端路径为文件夹时是否删除该目录
     * @throws Exception
     */
    public static void delete(ChannelSftp sftp, String remotePath, boolean rmThisDir) throws Exception {
        // sftp为null、或sftp是关闭的
        if (sftp == null || sftp.isClosed()) {
            throw new RuntimeException("sftp文件下载失败");
        }
        log.debug("sftp rm {}", remotePath);
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
                String remoteDirPath = remotePath + path;
                // 删除这个文件夹：不在内部处理，以减少一次请求
                delete(sftp, remoteDirPath, false);
                // 删除这个文件夹
                sftp.rmdir(remoteDirPath);
            } else {
                // 为文件直接删除
                String remoteFilePath = remotePath;
                if (!remotePath.endsWith(entry.getFilename())) {
                    // 远程文件路径不以文件名结尾
                    remoteFilePath += path;
                }
                // 删除文件
                sftp.rm(remoteFilePath);
            }
        }
        // 如果是当前远程目录是文件夹，则删除文件夹
        if (rmThisDir && isDir(sftp, remotePath)) {
            sftp.rmdir(remotePath);
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
