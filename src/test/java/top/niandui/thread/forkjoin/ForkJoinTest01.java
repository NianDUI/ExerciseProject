package top.niandui.thread.forkjoin;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * <a href="https://blog.csdn.net/f641385712/article/details/83749798">Java线程池之---ForkJoinPool线程池的使用以及原理</a>
 *
 * @author liyongda
 * @version 1.0
 * @date 2022/3/7 15:46
 */
public class ForkJoinTest01 {

    @Test
    public void test01() {
        long[] numbers = LongStream.rangeClosed(0, 1000000).toArray();

        {
            ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            Instant start = Instant.now();
            Long result = pool.invoke(new SumTask(numbers, 0, numbers.length - 1));
            Instant end = Instant.now();
            System.out.println("pool.getActiveThreadCount() = " + pool.getActiveThreadCount());
            System.out.println("pool.getQueuedTaskCount() = " + pool.getQueuedTaskCount());
            System.out.println("pool.getRunningThreadCount() = " + pool.getRunningThreadCount());
            pool.shutdown();
            System.out.println("耗时：" + Duration.between(start, end).toMillis() + "ms");
            System.out.println("result = " + result);
        }
        {
            Instant start = Instant.now();
            long result = LongStream.of(numbers).parallel().reduce(0, Long::sum);
            Instant end = Instant.now();
            System.out.println("耗时：" + Duration.between(start, end).toMillis() + "ms");
            System.out.println("result = " + result);
        }
    }

    //执行任务RecursiveTask：有返回值  RecursiveAction：无返回值
    private static class SumTask extends RecursiveTask<Long> {
        private final long[] numbers;
        private final int from;
        private final int to;

        public SumTask(long[] numbers, int from, int to) {
            this.numbers = numbers;
            this.from = from;
            this.to = to;
        }

        //此方法为ForkJoin的核心方法：对任务进行拆分  拆分的好坏决定了效率的高低
        @Override
        protected Long compute() {

            // 当需要计算的数字个数小于6时，直接采用for loop方式计算结果
            if (to - from < 6) {
                long total = 0;
                for (int i = from; i <= to; i++) {
                    total += numbers[i];
                }
                return total;
            } else { // 否则，把任务一分为二，递归拆分(注意此处有递归)到底拆分成多少分 需要根据具体情况而定
                int middle = (from + to) / 2;
                SumTask taskLeft = new SumTask(numbers, from, middle);
                SumTask taskRight = new SumTask(numbers, middle + 1, to);
                taskLeft.fork();
                taskRight.fork();
                return taskLeft.join() + taskRight.join();
            }
        }
    }
}
