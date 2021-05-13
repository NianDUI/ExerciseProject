package top.niandui.common.uitls.redis;

import org.springframework.stereotype.Component;
import top.niandui.common.base.BaseRedisLockUtil;
import top.niandui.common.uitls.GetInfo;

/**
 * Redis锁工具
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/9 13:40
 */
@Component
public class RedisLockUtil extends BaseRedisLockUtil {

    public RedisLockUtil(RedisByteUtil util) {
        super(util);
    }

    /**
     * (尝试)获取ip锁，值里包含本机ip
     *
     * @param key      键
     * @param lockTime 加锁时间。单位：秒(s)
     * @return true成功 false失败
     */
    public boolean tryLockIp(String key, long lockTime) {
        byte[] bytes = util.get(key);
        if (bytes == null) {
            // 尝试获取锁
            return tryLock(key, lockTime);
        } else {
            boolean contains = new String(bytes).contains(GetInfo.SERVICE_IP);
            if (contains) {
                // 值里面包含本ip，刷新缓存时间
                util.expire(key, lockTime);
            }
            return contains;
        }
    }
}
