package top.niandui.utils;

import lombok.SneakyThrows;
import sun.security.action.GetPropertyAction;

import java.security.AccessController;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Title: PrintUtil.java
 * @description: 控制台打印输出工具
 * @time: 2020/2/13 9:49
 * @author: liyongda
 * @version: 1.0
 */
public class PrintUtil {
    // 阻塞队列
    private static final BlockingQueue<String> PRINT_QUEUE = new LinkedBlockingQueue<>();
    // 换行符
    public static final String NEW_LINE = AccessController.doPrivileged(new GetPropertyAction("line.separator"));

    // 打印线程
    private static class Output implements Runnable {

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                System.out.print(PRINT_QUEUE.take());
            }
        }
    }

    // 打印线程初始化
    static {
        Thread thread = new Thread(new Output());
        thread.setDaemon(true);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    /**
     * 无换行输出
     *
     * @param content 控制台打印的内容
     * @return
     */
    public static boolean print(Object content) {
        return PRINT_QUEUE.add(content.toString());
    }

    /**
     * 有换行输出
     *
     * @param content 控制台打印的内容
     * @return
     */
    public static boolean println(Object content) {
        return print(content.toString() + NEW_LINE);
    }

    /**
     * 打印队列是否为空
     *
     * @return
     */
    public static boolean isEmpty() {
        return PRINT_QUEUE.isEmpty();
    }

    /**
     * 等待打印结束
     *
     * @param waitTime 每次等待休眠时间
     */
    public static void waitPrintEnd(long waitTime) {
        while (!PrintUtil.isEmpty()) {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
            }
        }
    }
}
