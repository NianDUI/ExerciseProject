package top.niandui.common.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import top.niandui.common.uitls.redis.RedisByteUtil;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Redis锁工具基类
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/9 13:40
 */
@Slf4j
public abstract class BaseRedisLockUtil {
    // 锁前缀
    public static final String LOCK = "lock:";
    // 锁线程池执行器
    protected static final ThreadPoolExecutor POOL = new ThreadPoolExecutor(2, 5, 5
            , TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new CustomizableThreadFactory("redis-lock-pool-"));
    // 1 byte[]
    protected static final byte[] BYTES1 = new byte[]{1};
    // 休眠时间20ms
    protected static final long SLEEP_TIME = 20;
    protected final RedisByteUtil util;

    public BaseRedisLockUtil(RedisByteUtil util) {
        this.util = util;
    }

    public RedisByteUtil getUtil() {
        return util;
    }

    //***********************************锁(一步操作)***********************************/

    /**
     * 释放锁
     *
     * @param key 键
     */
    public void unlock(String key) {
        util.del(checkKey(key));
    }

    /**
     * 获取锁
     * <P>循环尝试获取锁, 直到获取锁</P>
     *
     * @param key 键
     */
    public void lock(String key) {
        try {
            String checkKey = checkKey(key);
            while (!util.setNx(checkKey, BYTES1)) {
                Thread.sleep(SLEEP_TIME);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 获取锁
     * <P>循环尝试获取锁, 直到获取锁</P>
     *
     * @param key      键
     * @param lockTime 加锁时间。单位：秒(s)
     */
    public void lock(String key, long lockTime) {
        try {
            String checkKey = checkKey(key);
            while (!util.setNx(checkKey, BYTES1, lockTime)) {
                Thread.sleep(SLEEP_TIME);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * (尝试)获取锁
     * <P>尝试获取锁后直接返回</P>
     *
     * @param key 键
     * @return true成功 false失败
     */
    public boolean tryLock(String key) {
        return util.setNx(checkKey(key), BYTES1);
    }

    /**
     * (尝试)获取锁
     * <P>尝试获取锁后直接返回</P>
     *
     * @param key      键
     * @param lockTime 加锁时间。单位：秒(s)
     * @return true成功 false失败
     */
    public boolean tryLock(String key, long lockTime) {
        return util.setNx(checkKey(key), BYTES1, lockTime);
    }

    /**
     * (尝试)获取锁
     * <P>尝试在timeout时间内获取锁</P>
     *
     * @param key     键
     * @param timeout 等待时间
     * @param unit    等待的时间单位
     * @return true成功 false失败
     */
    public boolean tryLock(String key, long timeout, TimeUnit unit) {
        String checkKey = checkKey(key);
        Future<Boolean> future = POOL.submit(() -> {
            while (!util.setNx(checkKey, BYTES1)) {
                Thread.sleep(SLEEP_TIME);
            }
            return true;
        });
        return getFuture(future, timeout, unit);
    }

    /**
     * (尝试)获取锁
     * <P>尝试在timeout时间内获取锁</P>
     *
     * @param key      键
     * @param lockTime 加锁时间。单位：秒(s)
     * @param timeout  等待时间
     * @param unit     等待的时间单位
     * @return true成功 false失败
     */
    public boolean tryLock(String key, long lockTime, long timeout, TimeUnit unit) {
        String checkKey = checkKey(key);
        Future<Boolean> future = POOL.submit(() -> {
            while (!util.setNx(checkKey, BYTES1, lockTime)) {
                Thread.sleep(SLEEP_TIME);
            }
            return true;
        });
        return getFuture(future, timeout, unit);
    }

    //***********************************工具方法***********************************/

    /**
     * 检查key是否以"lock:"开头, 不是则加上。
     *
     * @param key 键
     * @return 以"lock:"开头的key
     */
    protected String checkKey(String key) {
        return key.startsWith(LOCK) ? key : LOCK + key;
    }

    /**
     * 获取结果, 最多等待timeout时间。
     *
     * @param future  结果获取对象
     * @param timeout 等待时间
     * @param unit    等待的时间单位
     * @return true获取到 false未获取到
     */
    protected boolean getFuture(Future<Boolean> future, long timeout, TimeUnit unit) {
        try {
            return future.get(timeout, unit);
        } catch (Exception e) {
            // 中断执行
            future.cancel(true);
            return false;
        }
    }

}
