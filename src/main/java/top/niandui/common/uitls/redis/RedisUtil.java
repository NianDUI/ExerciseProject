package top.niandui.common.uitls.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.niandui.common.base.BaseRedisUtil;

/**
 * redis 工具类
 *
 * @author huangwx
 * @version 1.0
 * @date 2019/11/18 9:01
 */
@Component
public class RedisUtil extends BaseRedisUtil<Object> {

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }
}
