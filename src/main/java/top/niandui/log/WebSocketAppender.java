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
        // 判断有没有开启处理
        if (!isStarted()) {
            return;
        }
        // 此步骤避免了 LBCLASSIC-139
        if (event instanceof DeferredProcessingAware) {
            ((DeferredProcessingAware) event).prepareForDeferredProcessing();
        }
        // 处理事件，返回日志json字符串
        String txt = processEvent(event);
        // 写数据
        writeString(txt);
    }

    /**
     * 处理事件，返回日志json字符串
     *
     * @param event 事件对象
     * @return 日志json字符串
     */
    private String processEvent(E event) {
        // 格式化消息
        String txt = this.layout.doLayout(event);
        // 对ERROR中的错误信息详情进行处理
        String errorDetail = "";
        int lastIndex = txt.lastIndexOf('}') + 1;
        if (lastIndex < txt.length()) {
            // 截取错误详情
            errorDetail = "<br/>" + txt.substring(lastIndex)
                    .replaceAll("\r?\n", "<br/>")
                    .replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
            // 截取json字符串
            txt = txt.substring(0, lastIndex);
        }
        // 将msg加入json字符串
        if (event instanceof LoggingEvent) {
            try {
                LoggingEvent le = (LoggingEvent) event;
                // 处理响应json字符串
                txt = new StringBuilder(txt.substring(0, txt.length() - 1))
                        // 拼接信息字段
                        .append(",\"msg\":")
                        .append(json.writeValueAsString(le.getFormattedMessage()))
                        // 拼接错误详情字段
                        .append(",\"errorDetail\":")
                        .append(json.writeValueAsString(errorDetail))
                        // 拼接结束符
                        .append(txt.substring(txt.length() - 1))
                        .toString();
            } catch (JsonProcessingException e) {
            }
        }
        return txt;
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
