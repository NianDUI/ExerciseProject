package top.niandui.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;
import top.niandui.common.uitls.RSAUtil;
import top.niandui.config.PublicBean;

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
@Slf4j
@Component
@ServerEndpoint(value = "/api/log/ws") //WebSocket客户端建立连接的地址
public class LogWebSocketServer {
    // 日志队列
    public static final LinkedBlockingQueue<String> LOG_QUEUE = new LinkedBlockingQueue<>();
    // 链接会话集合
    private static final Set<Session> LINK_SESSIONS = new HashSet<>();
    // 授权会话集合
    private static final Set<Session> AUTHORIZE_SESSIONS = new HashSet<>();
    // 发送消息线程
    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(1, 1, 5L
            , TimeUnit.MILLISECONDS, new SynchronousQueue<>(), new CustomizableThreadFactory("log-ws-pool-"));

    static {
        EXECUTOR.submit(() -> {
            while (true) {
                try {
                    String txt = LOG_QUEUE.take();
                    if (txt.length() > 500) {
                        // 长度超过500，截取前500个字符
                        txt = txt.substring(0, 500);
                    }
                    for (Session session : AUTHORIZE_SESSIONS) {
                        // 改为使用异步
                        session.getBasicRemote().sendText(txt);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    // token
    private final String token;
    // 是否校验token
    private final boolean isCheck;

    public LogWebSocketServer() {
        token = PublicBean.configInfo.getToken();
        isCheck = !StringUtils.isEmpty(token);
    }

    /**
     * 建立连接
     *
     * @param session 会话对象
     */
    @OnOpen
    public void onOpen(Session session) {
        LINK_SESSIONS.add(session);
        if (!isCheck) {
            AUTHORIZE_SESSIONS.add(session);
        }
    }

    /**
     * 关闭连接
     *
     * @param session 会话对象
     */
    @OnClose
    public void onClose(Session session) {
        LINK_SESSIONS.remove(session);
        AUTHORIZE_SESSIONS.remove(session);
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

    /**
     * 接收消息
     *
     * @param session 会话对象
     * @param message 接收到的消息
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        if (isCheck && token.equals(RSAUtil.privateKeyDecrypt(message))) {
            AUTHORIZE_SESSIONS.add(session);
        }
    }

}
