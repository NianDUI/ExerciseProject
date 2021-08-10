package top.niandui.douyu_websocket;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.*;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * 斗鱼弹幕
 * <h1>这个是失败的</h1>
 * <p><a href="https://www.cnblogs.com/aadd123/p/14009467.html">通过websocket抓取斗鱼弹幕和礼物消息</a></p>
 *
 * <p><a href="https://www.jianshu.com/p/346f30f176ff">调用斗鱼API爬取直播间弹幕信息（用户昵称及弹幕内容）</a></p>
 * <p><a href="https://blog.csdn.net/weixin_34049948/article/details/91771013">详解-斗鱼弹幕API-接入(斗鱼弹幕服务器第三方接入协议)</a></p>
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/7/28 14:18
 */
public class DanMuTest01 {

    public static void main(String[] args) throws Exception {
        BinaryWebSocketHandler webSocketHandler = new BinaryWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                super.afterConnectionEstablished(session);
                System.out.println("连接建立后 = " + session);
            }

            @Override
            protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
                super.handlePongMessage(session, message);
                System.out.println("handlePongMessage = " + message);
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                super.handleTransportError(session, exception);
                System.out.println("处理传输错误 = " + exception);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
                super.afterConnectionClosed(session, status);
                System.out.println("连接关闭后 = " + status);
            }

            @Override
            public boolean supportsPartialMessages() {
                boolean supportsPartialMessages = super.supportsPartialMessages();
                System.out.println("支持部分消息 = " + supportsPartialMessages);
                return supportsPartialMessages;
            }

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                System.out.println("处理消息 = " + message);
                super.handleMessage(session, message);
            }

            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) {
                System.out.println("处理文本消息 = " + message);
            }

            @Override
            protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
                bytesToMsg(message.getPayload());
            }
        };
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add("Host", "danmuproxy.douyu.com:8501");
        headers.add("Connection", "Upgrade");
        headers.add("Pragma", "no-cache");
        headers.add("Cache-Control", "no-cache");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");
        headers.add("Upgrade", "websocket");
        headers.add("Origin", "https://www.douyu.com");
        headers.add("Sec-WebSocket-Version", "13");
        headers.add("Accept-Encoding", "gzip, deflate, br");
        headers.add("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        headers.add("Sec-WebSocket-Key", "p8zDshn23IjaG8hN9FIzSQ==");
        headers.add("Sec-WebSocket-Extensions", "permessage-deflate; client_max_window_bits");

        StandardWebSocketClient client = new StandardWebSocketClient();
//        client.setUserProperties();
        String url = "wss://danmuproxy.douyu.com:8501";
//        String url = "ws://192.168.3.10:8001/api/log/ws";
        String rid = "288016";
        ListenableFuture<WebSocketSession> doHandshake = client.doHandshake(webSocketHandler, headers, URI.create(url));
        try (StandardWebSocketSession session = (StandardWebSocketSession) doHandshake.get()) {
            // 登录
            String loginreq = "type@=loginreq/roomid@=" + rid + "/dfl@=sn@AA=105@ASss@AA=1/username@=88380680/" +
                    "uid@=88380680/ver@=20190610/aver@=218101901/ct@=0/";
            session.sendMessage(new BinaryMessage(msgToBytes(loginreq)));
            // 接口一条消息
            Thread.sleep(1000 * 10);
            // 加入组消息：#gid默认1，此处改成 -9999 改成海量弹幕模式
            String joingroup = "type@=joingroup/rid@=" + rid + "/gid@=1/";
            session.sendMessage(new BinaryMessage(msgToBytes(joingroup)));
            // 心跳消息
            String mrkl = "type@=mrkl/";
            session.sendMessage(new BinaryMessage(msgToBytes(mrkl)));
            // 屏蔽礼物消息：
            String dmfbdreq = "type@=dmfbdreq/dfl@=sn@AA=105@ASss@AA=1@AS@Ssn@AA=106@ASss@AA=1@AS@Ssn@AA=107@ASss@AA=1@AS@Ssn@AA=108@ASss@AA=1@AS@Ssn@AA=110@ASss@AA=1@AS@Ssn@AA=901@ASss@AA=1@AS@S/";
            session.sendMessage(new BinaryMessage(msgToBytes(dmfbdreq)));

            Thread.sleep(1000 * 25);
            // 退出登录
            String logout = "type@=logout/";
            session.sendMessage(new BinaryMessage(msgToBytes(logout)));
            Thread.sleep(1000 * 1);

           /* session.sendMessage(new TextMessage("T+Hxm5XhXCFAAZx4r3GqfmU63TAYvV4zdJ90Q9wRcPR2oEsKNCK2QVfADh3UP3YLwNdkYHxMYqn66OAB/q+WDg=="));

            Thread.sleep(1000 * 25);*/
        }

    }

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
        buffer.put("\0".getBytes(StandardCharsets.UTF_8));
//        buffer.put((byte) 0);
        System.out.printf("↑ %d\t%d\t%s\n", size, 689, msg);
        return buffer;
    }

    public static String bytesToMsg(ByteBuffer buffer) {
        byte[] bytes = buffer.array();
        // 重置缓冲区
        buffer.rewind();
        // 设置为小端整数模式
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        // 消息长度
        int i1 = buffer.getInt(0);
        // 头部-消息长度：4 字节小端整数，表示整条消息（包括自身）长度（字节数）。
        int i2 = buffer.getInt(4);
        // 头部-消息类型：2 字节小端整数，表示消息类型。取值如下：
        //689 客户端发送给弹幕服务器的文本格式数据
        //690 弹幕服务器发送给客户端的文本格式数据。
        //头部-加密字段：暂时未用，默认为 0。头部-保留字段：暂时未用，默认为 0。
        int i3 = buffer.getShort(8);
        int i4 = buffer.get(10);
        int i5 = buffer.get(11);
        // 数据部分：斗鱼独创序列化文本数据，结尾必须为‘\0’。
        String msg = new String(bytes, 12, bytes.length - 12 - 1);
        System.out.printf("↓ %d\t%d\t%s\n", i1, i3, msg);
        return msg;
    }


}
