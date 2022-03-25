package top.niandui.common.uitls;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.jcraft.jsch.*;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 李永达
 * @version 1.0
 * @Title SshUtil.java
 * @description Ssh工具
 * @time 2021/12/10 11:03
 */
public class SshUtil {
    // 分隔符字符
    public static final char SEPARATOR_CHAR = '/';
    // 日志对象
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SshUtil.class);
    // 会话映射
    private static final ThreadLocal<Map<String, Session>> SESSION_MAP = ThreadLocal.withInitial(LinkedHashMap::new);

    static {
        // 设置当前日志打印机级别
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger(SshUtil.class);
        logger.setLevel(Level.DEBUG);
    }

    private SshUtil() {
    }

    /**
     * 连接会话
     *
     * @param host     主机地址
     * @param port     端口号
     * @param username 用户名
     * @param password 密码
     * @return 会话对象
     */
    public static synchronized Session connectSession(String host, int port, String username, String password) {
        try {
            String key = host + ":" + port + ":" + username;
            Session session = SESSION_MAP.get().get(key);
            if (session == null) {
                // 创建JSch对象
                JSch jsch = new JSch();
                // 通过用户名、主机、端口号获取会话对象
                session = jsch.getSession(username, host, port);
                // 设置连接密码
                session.setPassword(password);
                // 不验证主机密钥
                session.setConfig("StrictHostKeyChecking", "no");
                // 设置发送保持活动消息的间隔
//                session.setServerAliveInterval(60);
                log.debug("建立会话: ssh {}@{} -p {}", session.getUserName(), session.getHost(), session.getPort());
                session.connect();
                // 保存会话
                SESSION_MAP.get().put(key, session);
            }
            return session;
        } catch (Exception e) {
            throw new RuntimeException("ssh会话连接错误", e);
        }
    }

    /**
     * 关闭全部会话
     */
    public static synchronized void disconnectAllSession() {
        List<Session> values = SESSION_MAP.get().values().stream().sorted(Collections.reverseOrder()).collect(Collectors.toList());
        for (Session session : values) {
            disconnectSession(session);
        }
    }

    /**
     * 关闭会话
     *
     * @param session 会话对象
     */
    public static synchronized void disconnectSession(Session session) {
        try {
            // 本地端口转发:[15555:172.26.35.75:5555]
            String[] portForwardingLs = session.getPortForwardingL();
            for (String portForwardingL : portForwardingLs) {
                log.debug("关闭本地端口转发: ssh -N -L {} {}@{} -p {}", portForwardingL
                        , session.getUserName(), session.getHost(), session.getPort());
                session.delPortForwardingL(Integer.parseInt(portForwardingL.substring(0, portForwardingL.indexOf(':'))));
            }
            String key = session.getHost() + ":" + session.getPort() + ":" + session.getUserName();
            log.debug("关闭会话: ssh {}@{} -p {}", session.getUserName(), session.getHost(), session.getPort());
            session.disconnect();
            SESSION_MAP.get().remove(key);
        } catch (Exception e) {
            log.error("ssh会话关闭失败", e);
        }
    }

    /**
     * 关闭通道
     *
     * @param channel 通道对象
     * @param isDisconnectSession 是否关闭会话：t 关闭、f 不关闭
     */
    public static void disconnectChannel(Channel channel, boolean isDisconnectSession) {
        try {
            channel.disconnect();
            if (isDisconnectSession) {
                // 关闭会话
                disconnectSession(channel.getSession());
            }
        } catch (Exception e) {
            log.error("通道关闭失败", e);
        }
    }

    /* ************************** 注册本地端口转发 ************************** */
    /**
     * 为环回接口注册本地端口转发。如果lport为0，则将分配 tcp 端口。
     *
     * @param session ssh会话
     * @param lPort   用于本地端口转发的本地端口
     * @param rHost   本地端口转发的主机地址
     * @param rPort   本地端口转发的远程端口号
     * @return 分配的本地 TCP 端口号
     */
    public static int setPortForwardingL(Session session, int lPort, String rHost, int rPort) {
        return setPortForwardingL(session, "127.0.0.1", lPort, rHost, rPort);
    }

    /**
     * 为环回接口注册本地端口转发。如果lport为0，则将分配 tcp 端口。
     *
     * @param session ssh会话
     * @param lHost   用于本地端口转发的本地地址：如果是空字符串或"*" ，则端口应该可从所有接口使用。 如果是"localhost"或未给出，则侦听端口将被绑定仅供本地使用。
     * @param lPort   用于本地端口转发的本地端口
     * @param rHost   本地端口转发的主机地址
     * @param rPort   本地端口转发的远程端口号
     * @return 分配的本地 TCP 端口号
     */
    public static int setPortForwardingL(Session session, String lHost, int lPort, String rHost, int rPort) {
        return setPortForwardingL(session, String.format("%s:%s:%s:%s", lHost == null ? "*" : lHost, lPort, rHost, rPort));
    }

    /**
     * 为环回接口注册本地端口转发。如果lport为0，则将分配 tcp 端口。
     * <br>参数的格式应类似于“[lHost:]lPort:rHost:rPort”。
     * <br>如果lHost是空字符串或"*" ，则端口应该可从所有接口使用。
     * <br>如果lHost是"localhost"或未给出，则侦听端口将被绑定仅供本地使用。
     *
     * @param session ssh会话
     * @param conf    本地端口转发的配置
     * @return 分配的本地 TCP 端口号
     */
    public static int setPortForwardingL(Session session, String conf) {
        String[] confs = conf.split(":");
        int length = confs.length;
        if (length < 3 || length > 4) {
            throw new RuntimeException("参数格式错误");
        }
        try {
            // 为环回接口注册本地端口转发
            int lPort = session.setPortForwardingL(conf);
            confs[length == 3 ? 0 : 1] = Integer.toString(lPort);
            log.debug("注册本地端口转发: ssh -N -L {} {}@{} -p {}", String.join(":", confs)
                    , session.getUserName(), session.getHost(), session.getPort());
            return lPort;
        } catch (JSchException e) {
            throw new RuntimeException("注册本地端口转发错误", e);
        }
    }

    /* ************************** Shell相关 ************************** */
    /**
     * 打开Shell通道
     *
     * @param session 会话对象
     * @return Shell通道对象
     */
    public static ChannelShell openChannelShell(Session session) {
        try {
            // 打开sftp通道
            ChannelShell channel = (ChannelShell)  session.openChannel("shell");
            channel.setPty(true);
            channel.setPtyType("vt100", 150, 24, 1200, 480);
            // 建立通道
            channel.connect();
            return channel;
        } catch (Exception e) {
            throw new RuntimeException("shell连接错误", e);
        }
    }

    /* ************************** Exec相关 ************************** */
    /**
     * exec通道执行指令
     *
     * @param session 会话对象
     * @param command 指令
     * @param args 指令参数数组
     * @return 指令返回
     */
    public static String channelExecCommand(Session session, String command, String... args) {
        try {
            // 打开sftp通道
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            InputStream is = channel.getInputStream();
            channel.setErrStream(System.err);
            channel.setCommand(String.format(command, args));
            // 建立通道
            channel.connect();
            String result = FileCopyUtils.copyToString(new InputStreamReader(is));
            channel.disconnect();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("exec执行指令错误：" + command, e);
        }
    }

    /* ************************** Sftp相关 ************************** */
    /**
     * 打开sftp通道
     *
     * @param session 会话对象
     * @return sftp通道对象
     */
    public static ChannelSftp openChannelSftp(Session session) {
        try {
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
     * 从sftp服务器上下载指定文件或文件夹，到本地指定的目录
     *
     * @param sftp       sftp通道对象
     * @param remotePath 远程路径
     * @param localPath  本地路径
     * @throws Exception
     */
    public static void download(ChannelSftp sftp, String remotePath, String localPath) throws Exception {
        // 检查sftp通道对象
        checkChannelSftp(sftp, "sftp文件下载失败");
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
     * 从sftp服务器上下载指定文件，到本地指定的文件
     *
     * @param sftp       sftp通道对象
     * @param remoteFile 远程文件路径
     * @param localFile  本地文件路径
     * @throws Exception
     */
    public static void downloadFile(ChannelSftp sftp, String remoteFile, String localFile) throws Exception {
        // 检查sftp通道对象
        checkChannelSftp(sftp, "sftp文件下载失败");
        log.debug("sftp download {} > {}", remoteFile, localFile);
        sftp.get(remoteFile, localFile);
    }

    /**
     * 从sftp服务器上下载指定文件，到文件输出流
     *
     * @param sftp       sftp通道对象
     * @param remoteFile 远程文件路径
     * @param os         输出流
     * @throws Exception
     */
    public static void downloadFile(ChannelSftp sftp, String remoteFile, OutputStream os) throws Exception {
        // 检查sftp通道对象
        checkChannelSftp(sftp, "sftp文件下载失败");
        log.debug("sftp download {} > OutputStream", remoteFile);
        sftp.get(remoteFile, os);
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
        // 检查sftp通道对象
        checkChannelSftp(sftp, "sftp文件上传失败");
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
        // 检查sftp通道对象
        checkChannelSftp(sftp, "sftp文件删除失败");
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

    /**
     * 检查sftp通道对象
     *
     * @param sftp    sftp通道对象
     * @param message 异常信息
     */
    public static void checkChannelSftp(ChannelSftp sftp, String message) {
        // sftp为null、或sftp是关闭的
        if (sftp == null || sftp.isClosed()) {
            throw new RuntimeException(message);
        }
    }


}
