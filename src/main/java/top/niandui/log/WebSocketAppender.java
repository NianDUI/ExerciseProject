package top.niandui.log;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.spi.DeferredProcessingAware;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;
import top.niandui.websocket.LogWebSocketServer;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 追加日志到WebSocket
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/18 14:04
 */
public class WebSocketAppender<E> extends UnsynchronizedAppenderBase<E> {
    // 可重入锁
    private final ReentrantLock lock = new ReentrantLock(false);
    // json格式化器
    private final ObjectMapper json = new ObjectMapper();
    // 布局对象
    private Layout<E> layout;

    public void setLayout(Layout<E> layout) {
        this.layout = layout;
    }

    /**
     * 添加日志事件
     *
     * @param event 事件对象
     */
    @Override
    protected void append(E event) {
        if (!isStarted()) {
            return;
        }
        // 此步骤避免了 LBCLASSIC-139
        if (event instanceof DeferredProcessingAware) {
            ((DeferredProcessingAware) event).prepareForDeferredProcessing();
        }
        // 格式化消息
        String txt = this.layout.doLayout(event);
        // 将msg加入json字符串
        if (event instanceof LoggingEvent) {
            try {
                LoggingEvent le = (LoggingEvent) event;
                // 处理msg字符串为json字符串
                txt = new StringBuilder(txt.substring(0, txt.length() - 1))
                        .append(",\"msg\":")
                        .append(json.writeValueAsString(le.getFormattedMessage()))
                        .append(txt.substring(txt.length() - 1))
                        .toString();
            } catch (JsonProcessingException e) {
            }
        }
        writeString(txt);
    }

    /**
     * 同步写字符串
     *
     * @param txt 文本
     */
    private void writeString(String txt) {
        if (!StringUtils.hasText(txt)) {
            return;
        }
        lock.lock();
        try {
            LogWebSocketServer.LOG_QUEUE.offer(txt);
        } finally {
            lock.unlock();
        }
    }
}
