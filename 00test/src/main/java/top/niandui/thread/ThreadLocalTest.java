package top.niandui.thread;

import lombok.SneakyThrows;

/**
 * ThreadLocal 类测试
 */
public class ThreadLocalTest {
    public static void main(String[] args) {
        ThreadLocal<Integer> local = ThreadLocal.withInitial(() -> 0);

        Runnable runnable = () -> {
            while (true) {
                Integer integer = local.get();
                System.out.println(Thread.currentThread().getName() + " : "  + integer);
                integer++;
                local.set(integer);
                if (integer > 5) {
                    break;
                }
                Thread.yield();
            }
        };

        new Thread(runnable).start();
        new Thread(runnable).start();
    }
}
