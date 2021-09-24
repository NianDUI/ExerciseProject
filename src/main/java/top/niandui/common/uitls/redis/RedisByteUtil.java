package top.niandui.common.uitls.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import top.niandui.common.base.BaseRedisUtil;

/**
 * redis byte(二进制) 工具类
 *
 * @author liyongda
 * @version 1.0
 * @date 2019/12/30 16:02
 */
@Component
public class RedisByteUtil extends BaseRedisUtil<byte[]> {

    public RedisByteUtil(RedisConnectionFactory factory) {
        super(new RedisTemplate<>());
        redisTemplate.setConnectionFactory(factory);

        // key采用String的序列化方式
        redisTemplate.setKeySerializer(RedisSerializer.string());
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        // value序列化方式采用 自定义byte数组的序列化方式
        redisTemplate.setValueSerializer(RedisSerializer.byteArray());
        // hash的value序列化方式采用 自定义byte数组的序列化方式
        redisTemplate.setHashValueSerializer(RedisSerializer.byteArray());
        redisTemplate.afterPropertiesSet();
    }
}