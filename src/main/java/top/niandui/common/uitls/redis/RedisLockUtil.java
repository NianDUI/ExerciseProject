package top.niandui.common.uitls.redis;

import org.springframework.stereotype.Component;
import top.niandui.common.base.BaseRedisLockUtil;

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
}
