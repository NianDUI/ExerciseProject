package top.niandui.log;

import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.spi.DeferredProcessingAware;
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
