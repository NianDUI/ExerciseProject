package top.niandui.websocket;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 日志WebSocket服务器
 * <p>@OnOpen,@OnClose,@OnMessage,@OnError</p>
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/18 14:41
 */
@Component
@ServerEndpoint(value = "/api/log/ws") //WebSocket客户端建立连接的地址
public class LogWebSocketServer {
    // 日志队列
    public static final LinkedBlockingQueue<String> LOG_QUEUE = new LinkedBlockingQueue<>();
    // 会话集合
    private static final Set<Session> SESSIONS_SET = new HashSet<>();
    // 发送消息线程
    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(1, 1, 5L
            , TimeUnit.MILLISECONDS, new SynchronousQueue<>(), new CustomizableThreadFactory("log-ws-pool-"));

    static {
        EXECUTOR.submit(() -> {
            while (true) {
                try {
                    String txt = LOG_QUEUE.take();
                    for (Session session : SESSIONS_SET) {
                        session.getBasicRemote().sendText(txt);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 建立连接
     *
     * @param session 会话对象
     */
    @OnOpen
    public void onOpen(Session session) {
        SESSIONS_SET.add(session);
    }

    /**
     * 关闭连接
     *
     * @param session 会话对象
     */
    @OnClose
    public void onClose(Session session) {
        SESSIONS_SET.remove(session);
    }

    /**
     * 错误
     *
     * @param session 会话对象
     * @param error   错误对象
     */
    @OnError
    public void onError(Session session, Throwable error) {
    }

}
