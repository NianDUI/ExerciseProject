package top.niandui.common.uitls.redis;

import org.springframework.stereotype.Component;
import top.niandui.common.base.BaseRedisLockUtil;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Redis可重入锁工具
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/11 10:02
 */
@Component
public class RedisReentrantLockUtil extends BaseRedisLockUtil {
    // 操作锁的锁前缀
    public static final String LOCK_OPERATE = LOCK + "operate:";
    // 操作锁的锁时间
    protected static final long OPERATE_LOCK_TIME = 60;

    public RedisReentrantLockUtil(RedisByteUtil util) {
        super(util);
    }

    //***********************************可重入锁(多步操作)***********************************/

    /**
     * 释放可重入锁
     *
     * @param key 键
     */
    @Override
    public void unlock(String key) {
        operateLock(key, () -> {
            String checkKey = checkKey(key);
            // 释放可重入锁
            byte[] value = util.get(checkKey);
            if (value != null) {
                if (value[0] > 1) {
                    // 重入-1
                    value[0]--;
                    util.set(key, value);
                } else {
                    super.unlock(checkKey);
                }
            }
            System.out.println(Thread.currentThread().getName() + " 释放锁");
        });
    }

    /**
     * 获取可重入锁
     * <P>循环尝试获取锁, 直到获取锁</P>
     *
     * @param key 键
     */
    @Override
    public void lock(String key) {
        operateLock(key, () -> {
            String checkKey = checkKey(key);
            // 获取可重入锁
            byte[] value = util.get(checkKey);
            if (value == null) {
                super.lock(checkKey);
            } else {
                // 重入+1
                value[0]++;
                util.set(key, value);
            }
            System.out.println(Thread.currentThread().getName() + " 加锁");
        });
    }

    /**
     * 获取可重入锁
     * <P>循环尝试获取锁, 直到获取锁</P>
     *
     * @param key      键
     * @param lockTime 加锁时间。单位：秒(s)
     */
    @Override
    public void lock(String key, long lockTime) {
        operateLock(key, () -> {
            String checkKey = checkKey(key);
            // 获取可重入锁
            byte[] value = util.get(checkKey);
            if (value == null) {
                super.lock(checkKey, lockTime);
            } else {
                // 重置缓存时间
                util.expire(checkKey, lockTime);
                // 重入+1
                value[0]++;
                util.set(key, value);
            }
        });
    }

    /**
     * (尝试)获取可重入锁
     * <P>尝试获取锁后直接返回</P>
     *
     * @param key 键
     * @return true成功 false失败
     */
    @Override
    public boolean tryLock(String key) {
        return operateLock(key, () -> {
            String checkKey = checkKey(key);
            // 获取可重入锁
            byte[] value = util.get(checkKey);
            if (value == null) {
                return super.tryLock(checkKey);
            } else {
                // 重入+1
                value[0]++;
                util.set(key, value);
                return true;
            }
        });
    }

    /**
     * (尝试)获取可重入锁
     * <P>尝试获取锁后直接返回</P>
     *
     * @param key      键
     * @param lockTime 加锁时间。单位：秒(s)
     * @return true成功 false失败
     */
    @Override
    public boolean tryLock(String key, long lockTime) {
        return operateLock(key, () -> {
            String checkKey = checkKey(key);
            // 获取可重入锁
            byte[] value = util.get(checkKey);
            if (value == null) {
                return super.tryLock(checkKey, lockTime);
            } else {
                // 重置缓存时间
                util.expire(checkKey, lockTime);
                // 重入+1
                value[0]++;
                util.set(key, value);
                return true;
            }
        });
    }

    /**
     * (尝试)获取可重入锁
     * <P>尝试在timeout时间内获取锁</P>
     *
     * @param key     键
     * @param timeout 等待时间
     * @param unit    等待的时间单位
     * @return true成功 false失败
     */
    @Override
    public boolean tryLock(String key, long timeout, TimeUnit unit) {
        return operateLock(key, () -> {
            String checkKey = checkKey(key);
            // 获取可重入锁
            byte[] value = util.get(checkKey);
            if (value == null) {
                return super.tryLock(key, timeout, unit);
            } else {
                // 重入+1
                value[0]++;
                util.set(key, value);
                return true;
            }
        });
    }

    /**
     * (尝试)获取可重入锁
     * <P>尝试在timeout时间内获取锁</P>
     *
     * @param key      键
     * @param lockTime 加锁时间。单位：秒(s)
     * @param timeout  等待时间
     * @param unit     等待的时间单位
     * @return true成功 false失败
     */
    @Override
    public boolean tryLock(String key, long lockTime, long timeout, TimeUnit unit) {
        return operateLock(key, () -> {
            String checkKey = checkKey(key);
            // 获取可重入锁
            byte[] value = util.get(checkKey);
            if (value == null) {
                return super.tryLock(key, lockTime, timeout, unit);
            } else {
                // 重置缓存时间
                util.expire(checkKey, lockTime);
                // 重入+1
                value[0]++;
                util.set(key, value);
                return true;
            }
        });
    }

    //***********************************工具方法***********************************/

    /**
     * 对回调加锁, 并对操作进行回调
     *
     * @param key      键
     * @param callback 回调
     */
    protected void operateLock(String key, Runnable callback) {
        String operateKey = LOCK_OPERATE + key;
        try {
            // 操作加锁
            super.lock(operateKey, OPERATE_LOCK_TIME);
            callback.run();
        } finally {
            // 释放操作锁
            super.unlock(operateKey);
        }
    }

    /**
     * 对回调加锁, 并对操作进行回调
     *
     * @param key      键
     * @param callback 回调
     * @return 回调返回值
     */
    protected Boolean operateLock(String key, Supplier<Boolean> callback) {
        String operateKey = LOCK_OPERATE + key;
        try {
            // 操作加锁
            super.lock(operateKey, OPERATE_LOCK_TIME);
            return callback.get();
        } finally {
            // 释放操作锁
            super.unlock(operateKey);
        }
    }
}
