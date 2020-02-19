package top.niandui.utils;

import lombok.SneakyThrows;
import top.niandui.model.Info;
import top.niandui.model.WriteType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Title: FileUtil.java
 * @description: 文件写
 * @time: 2020/2/19 10:49
 * @author: liyongda
 * @version: 1.0
 */
public class FileWriter {
    // 内容队列
    private Queue<String> writeQueue = new LinkedBlockingQueue<>();
    // 信息对象
    private final Info info;
    // 带缓存的写对象
    private volatile BufferedWriter bw;
    // 写线程对象
    private volatile Thread output;

    public FileWriter(final Info info) {
        this.info = info;
    }

    /**
     * 初始化方法
     */
    public void init() {
        // BufferedWriter初始化
        getBw();
        if (info.writeType == WriteType.ONCE) {
            // 内容队列初始化
            writeQueue = new LinkedList<>();
        } else if (info.writeType == WriteType.REPEATEDLY) {
            // 内容队列初始化
            writeQueue = new LinkedBlockingQueue<>();
            // 写线程初始化和启动
            getOutput().start();
        }
    }

    /**
     * 获取和初始化带缓存的写对象
     *
     * @return 带缓存的写对象
     */
    @SneakyThrows
    private BufferedWriter getBw() {
        if (bw != null) {
            return bw;
        }
        File target = new File("target");
        if (!target.exists()) {
            target.mkdirs();
        }
        String pathname = "target/" + info.fileName;
        bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(pathname, info.isAppendWrite), StandardCharsets.UTF_8));
        return bw;
    }

    /**
     * 获取和初始化写线程对象
     *
     * @return 写线程对象
     */
    private Thread getOutput() {
        if (output != null) {
            return output;
        }
        output = new Thread(() -> {
            try {
                BlockingQueue<String> writeQueue = (BlockingQueue<String>) this.writeQueue;
                while (true) {
                    bw.write(writeQueue.take());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        output.setDaemon(true);
        output.setPriority(Thread.MIN_PRIORITY);
        return output;
    }

    /**
     * 写信息
     *
     * @param content 要写出的内容
     */
    public void write(Object content) {
        writeQueue.add(content.toString());
    }

    /**
     * 结束写
     */
    @SneakyThrows
    public void endWrite() {
        if (bw == null) {
            return;
        }
        if (info.writeType == WriteType.ONCE) {
            if (writeQueue != null) {
                // 一次性写入
                while (!isEmpty()) {
                    bw.write(writeQueue.poll());
                }
            }
        } else if (info.writeType == WriteType.REPEATEDLY) {
            // 等待写结束
            waitWaitEnd(100);
            // 停止写线线程
            if (output != null) {
                output = null;
            }
        }
        // close带缓存的写对象
        if (bw != null) {
            bw.flush();
            bw.close();
            bw = null;
        }
    }

    /**
     * 带缓存的写对象刷新
     */
    @SneakyThrows
    public void flush() {
        if (bw != null) {
            bw.flush();
        }
    }

    /**
     * 写队列是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return writeQueue.isEmpty();
    }

    /**
     * 等待写结束
     *
     * @param waitTime 每次等待休眠时间
     */
    @SneakyThrows
    public void waitWaitEnd(long waitTime) {
        while (!isEmpty()) {
            Thread.sleep(waitTime);
        }
    }

}
