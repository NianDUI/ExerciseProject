package top.niandui.thread.pool;

import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 李永达
 * @version 1.0
 * @Title ThreadPoolTest01.java
 * @description 线程池测试
 * @time 2021/4/19 16:59
 */
public class ThreadPoolTest01 {

    /**
     * 测试什么情况下线程数量会超过核心线程数，达到最大线程数
     */
    @Test
    public void test01() throws Exception {
//        Executors.newFixedThreadPool()
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 4, 5, TimeUnit.SECONDS
                // 可以完成目的
                , new SynchronousQueue<>()
                // 新增到核心线程数后，会往队列里面放，放满后会启用新线程，直到最大线程数，之后会根据拒绝策略进行
//                , new ArrayBlockingQueue<>(6)
                , new CustomizableThreadFactory("(自定义名称)pool-1-thread-")
                , (r, executor) -> System.out.println("拒绝执行处理程序" + Thread.currentThread().getName())
        );

        Runnable run = () -> {
            Thread thread = Thread.currentThread();
            System.out.println(thread.getName());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
        };
        for (int i = 0; i < 12; i++) {
            Runnable run1 = () -> {};
            System.out.println(run1);
        }
        for (int i = 0; i < 12; i++) {
            System.out.println("i = " + i + " " + pool.submit(run));
        }
    }
}
