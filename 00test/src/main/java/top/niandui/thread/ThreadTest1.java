package top.niandui.thread;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 线程测试类
 */
public class ThreadTest1 {
    static Logger log = LoggerFactory.getLogger(ThreadTest1.class);

    public static void main(String[] args) {
        new Thread1().start();
        new Thread1().start();

        final int[] i = {0};
        Runnable runnable = () -> {
            while (true) {
                synchronized (log) {
                    i[0]++;
                    log.debug(Thread.currentThread().getName() + " : " + i[0] + " : " + Thread.activeCount());
                }
                if (i[0] >= 5) {
                    break;
//                        Thread.currentThread().getThreadGroup().interrupt();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Thread.yield();
            }
        };
        new Thread(runnable).start();
        new Thread(runnable).start();

    }

    public static class Thread1 extends Thread {
        private int i = 0;

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                synchronized (log) {
                    i++;
                    log.debug(Thread.currentThread().getName() + " : " + i + " : " + Thread.activeCount());
                }
                if (i >= 5) {
                    break;
//                        Thread.currentThread().getThreadGroup().interrupt();
                }
                Thread.sleep(1000);
                Thread.yield();
            }
        }
    }
}
