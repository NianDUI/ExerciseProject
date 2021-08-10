package top.niandui.douyu_websocket;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 斗鱼弹幕
 * <h1>这个是成功获取到弹幕信息的</h1>
 * <p><a href="https://www.cnblogs.com/aadd123/p/14009467.html">通过websocket抓取斗鱼弹幕和礼物消息</a></p>
 * <p>使用依赖：org.java-websocket  Java-WebSocket  1.5.2</p>
 * <p>将Draft_6455类中的Framedata translateSingleFrame(ByteBuffe)方法最后frame.isValid();注释掉：用于防止ControlFrame.isValid()报错</p>
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/7/28 14:18
 */
@Slf4j
public class DanMuTest02 {
    // 弹幕服务器随机端口：8501-8506
    private static final String port = "850" + (int) (Math.random() * 6 + 1);
    // 弹幕服务器地址
    private static final String url = "wss://danmuproxy.douyu.com:" + port;
    // 请求头
    private static final Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Host", "danmuproxy.douyu.com:" + port);
        headers.put("Connection", "Upgrade");
        headers.put("Pragma", "no-cache");
        headers.put("Cache-Control", "no-cache");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");
        headers.put("Upgrade", "websocket");
        headers.put("Origin", "https://www.douyu.com");
        headers.put("Sec-WebSocket-Version", "13");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        headers.put("Sec-WebSocket-Key", "p8zDshn23IjaG8hN9FIzSQ==");
        headers.put("Sec-WebSocket-Extensions", "permessage-deflate; client_max_window_bits");

        // 设置日志显示等级
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = context.getLogger("root");
        root.setLevel(Level.INFO);
    }

    public static void main(String[] args) throws Exception {
        WebSocketClient client = new WebSocketClient(URI.create(url), new Draft_6455(), headers) {
            // 心跳线程池
            private final ThreadPoolExecutor mrklExecutor = new ThreadPoolExecutor(1, 1, 0L
                    , TimeUnit.SECONDS, new SynchronousQueue<>(), new CustomizableThreadFactory("mrkl-thread-"));
            // 房间id 288016
            private final String rid = "74751";

            @Override
            public void onOpen(ServerHandshake handshakedata) {

                log.info("弹幕服务器连接成功 {}", getURI());
                // 登录: "type@=loginreq/roomid@=" + rid + "/dfl@=sn@AA=105@ASss@AA=1/username@=88380680/uid@=88380680/ver@=20190610/aver@=218101901/ct@=0/"
                String loginreq = "type@=loginreq/roomid@=" + rid + "/";
                send(msgToBytes(loginreq));
                // 加入组消息：gid默认1，此处改成 -9999 改成海量弹幕模式
                String joingroup = "type@=joingroup/rid@=" + rid + "/gid@=1/";
                send(msgToBytes(joingroup));
                mrklExecutor.execute(() -> {
                    log.info("启动心跳线程");
                    try {
                        Thread thread = Thread.currentThread();
                        // 心跳消息
                        String mrkl = "type@=mrkl/";
                        while (!thread.isInterrupted()) {
                            // 线程池未被关闭
                            // 心跳消息
                            send(msgToBytes(mrkl));
                            Thread.sleep(1000 * 45);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    log.info("停止心跳线程");
                });
                // 屏蔽礼物消息：
                String dmfbdreq = "type@=dmfbdreq/dfl@=sn@AA=105@ASss@AA=1@AS@Ssn@AA=106@ASss@AA=1@AS@Ssn@AA=107@ASss@AA=1@AS@Ssn@AA=108@ASss@AA=1@AS@Ssn@AA=110@ASss@AA=1@AS@Ssn@AA=901@ASss@AA=1@AS@S/";
                send(msgToBytes(dmfbdreq));
            }

            @Override
            public void onMessage(String message) {
                log.info("弹幕服务器收到消息={}", message);
            }

            @Override
            public void onMessage(ByteBuffer bytes) {
                // 解析 bytes
                String msg = bytesToMsg(bytes);
                // 解析 msg
                Map<String, String> msgMap = parseMsg(msg);
                if ("chatmsg".equals(msgMap.get("type"))) {
                    // 只输出用户发送的消息
                    System.out.printf("lv%-5s%-20s：%s%n", msgMap.get("level"), msgMap.get("nn"), msgMap.get("txt"));
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                log.info("弹幕服务器退出连接。code={},reason={},remote={}", code, reason, remote);
            }

            @Override
            public void onError(Exception ex) {
                log.info("弹幕服务器连接错误={}", ex.getMessage());
            }

            @Override
            public void close() {
                // 退出登录
                String logout = "type@=logout/";
                send(msgToBytes(logout));
                // 立刻发送中断信号，终止所有线程，并返回一个停止线程的队列，要求线程能够正确响应中断。
                mrklExecutor.shutdownNow();
                super.close();
            }
        };
        // 启动 websocket 连接。 此方法不会阻塞.
        client.connect();

        Thread main = Thread.currentThread();
        // 让父线程等待子线程结束之后才能继续运行。其实只是让main线程暂停到这里了
        main.join();

        Thread.sleep(1000 * 120);
        client.close();
    }

    // 将字符串消息解析 ByteBuffer, 用于向弹幕服务器发送消息
    public static ByteBuffer msgToBytes(String msg) {
        byte[] msgBytes = msg.getBytes(StandardCharsets.UTF_8);
        int size = 4 + 2 + 1 + 1 + msgBytes.length + 1;
        ByteBuffer buffer = ByteBuffer.allocate(4 + size);
        // 设置为小端整数模式
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        // 消息长度
        buffer.putInt(size);
        // 头部-消息长度：4 字节小端整数，表示整条消息（包括自身）长度（字节数）。
        buffer.putInt(size);
        // 头部-消息类型：2 字节小端整数，表示消息类型。取值如下：
        //689 客户端发送给弹幕服务器的文本格式数据
        //690 弹幕服务器发送给客户端的文本格式数据。
        //头部-加密字段：暂时未用，默认为 0。头部-保留字段：暂时未用，默认为 0。
        buffer.putShort((short) 689);
        buffer.put((byte) 0);
        buffer.put((byte) 0);
        // 数据部分：斗鱼独创序列化文本数据，结尾必须为‘\0’。
        buffer.put(msgBytes);
//        buffer.put("\0".getBytes(StandardCharsets.UTF_8));
        buffer.put((byte) 0);
        log.debug("↑ {}\t{}\t{}", size, 689, msg);
        // 重置缓冲区，让缓冲区可读
        buffer.rewind();
        return buffer;
    }

    // 将 ByteBuffer 解析为字符串消息, 用于接收弹幕服务器发送消息
    public static String bytesToMsg(ByteBuffer buffer) {
        byte[] bytes = buffer.array();
        // 重置缓冲区，让缓冲区可读
        buffer.rewind();
        // 设置为小端整数模式
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        // 消息长度
        int size1 = buffer.getInt(0);
        // 头部-消息长度：4 字节小端整数，表示整条消息（包括自身）长度（字节数）。
        int size2 = buffer.getInt(4);
        // 头部-消息类型：2 字节小端整数，表示消息类型。取值如下：
        //689 客户端发送给弹幕服务器的文本格式数据
        //690 弹幕服务器发送给客户端的文本格式数据。
        //头部-加密字段：暂时未用，默认为 0。头部-保留字段：暂时未用，默认为 0。
        short type = buffer.getShort(8);
//        byte encryptedField = buffer.get(10);
//        byte reservedText = buffer.get(11);
        // 数据部分：斗鱼独创序列化文本数据，结尾必须为‘\0’。
        String msg = new String(bytes, 12, bytes.length - 12 - 1);
        log.debug("↓ {}\t{}\t{}", size1, type, msg);
        return msg;
    }

    // 将字符串消息解析为 Map<String, String>
    public static Map<String, String> parseMsg(String msg) {
        // ↓ 297	690	type@=chatmsg/rid@=288016/ct@=1/uid@=358963144/nn@=烟花易冷cold17322/txt@=玩的是自定义吗／？/cid@=5e87fe93079d4499d26b861600000000/ic@=avatar_v3@S202102@S452f3a40f8264d20aa54c4bc5163725b/level@=15/sahf@=0/cst@=1627874185/bnn@=/bl@=0/brid@=0/hc@=/el@=/lk@=/pdg@=91/pdk@=87/ext@=/
        Map<String, String> msgMap = new LinkedHashMap<>();
        String split = "@=";
        int index = -1, preIndex;
        while ((index = msg.indexOf('/', preIndex = index + 1)) > -1) {
            // 字符串中还存在'/', 并将上一个的索引值 + 1 复制给 preIndex
            String item = msg.substring(preIndex, index);
            // 判断消息是否为正常的消息
            int i = item.indexOf(split);
            if (i > -1) {
                // 消息正常，解析放入
                msgMap.put(item.substring(0, i), item.substring(i + split.length()));
            } else {
                // 消息不正常，直接放入
                msgMap.put(item, item);
            }
        }
        // 返回解析后的消息
        return msgMap;
    }

}
