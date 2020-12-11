package top.niandui.redis;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import top.niandui.common.uitls.redis.RedisByteUtil;
import top.niandui.common.uitls.redis.RedisLockUtil;
import top.niandui.common.uitls.redis.RedisReentrantLockUtil;
import top.niandui.common.uitls.redis.RedisUtil;
import top.niandui.model.Site;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * test
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/7 13:32
 */
public class RedisTest {
    static ExecutorService executor = Executors.newFixedThreadPool(10);
    public RedisUtil redisUtil;
    public RedisByteUtil redisByteUtil;
    public RedisLockUtil lockUtil;
    public RedisReentrantLockUtil reentrantLockUtil;

    private final ObjectMapper json = new ObjectMapper();

    {
        // 设置日志显示等级
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = context.getLogger("root");
        root.setLevel(Level.INFO);

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        LettuceConnectionFactory factory = new LettuceConnectionFactory("127.0.0.1", 6379);
        factory.afterPropertiesSet();
        template.setConnectionFactory(factory);

        // key采用String的序列化方式
        template.setKeySerializer(RedisSerializer.string());
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(RedisSerializer.string());
        // value序列化方式采用 自定义byte数组的序列化方式
        template.setValueSerializer(RedisSerializer.json());
        // hash的value序列化方式采用 自定义byte数组的序列化方式
        template.setHashValueSerializer(RedisSerializer.json());
        template.afterPropertiesSet();
        redisUtil = new RedisUtil(template);
        redisByteUtil = new RedisByteUtil(factory);
        lockUtil = new RedisLockUtil(redisByteUtil);
        reentrantLockUtil = new RedisReentrantLockUtil(redisByteUtil);
    }

    @Test
    public void testLock() throws Exception {
        String key = "key1";
        reentrantLockUtil.unlock(key);
        Runnable runnable = () -> {
            Thread thread = Thread.currentThread();
            while (true) {
                reentrantLockUtil.lock(key);
                reentrantLockUtil.lock(key);
                System.out.println(thread.getName() + " key = " + key);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reentrantLockUtil.unlock(key);
                reentrantLockUtil.unlock(key);
            }
        };
        for (int i = 0; i < 2; i++) {
            executor.submit(runnable);
        }
        Thread.sleep(20 * 1000);
    }

    @Test
    public void test2() throws Exception {
        redisUtil.set("test:json", 1);
        redisByteUtil.set("test:bytes", new byte[]{1});
        redisUtil.set("test:bytesjson", new byte[]{1});

        redisUtil.incr("test:json", 1);
    }

    @Test
    public void test1() throws Exception {
        Set<Object> set = redisUtil.sGet("asdfasdf");
        System.out.println("set = " + set);
    }

    @Test
    public void test() throws Exception {
        Map map = new HashMap<>();
        map.put("a", 1);
        map.put("Integr", 1);
        map.put("Long", 2L);
        map.put("Short", (short) 3);
        map.put("float", 1.1F);
        map.put("double", 1.2);
        Site site = new Site();
        site.setConfigid(1L);
        site.setCreatetime(new Timestamp(System.currentTimeMillis()));
        site.setName("sitename");
        site.setSiteid(2L);
        site.setUrl("ur;l");
        map.put("model", site);
        map.put("array", new int[]{1, 2, 3, 4, 5, 6});
        map.put("array2", new long[]{1L, 2L, 3L, 4L, 5L, 6L});
        map.put("list", Arrays.asList("1", "2", "3"));
        redisUtil.setNx("test", map);
        map.put("a", 2);
        redisUtil.set("test", map);

        Object test = redisUtil.get("test");
        System.out.println(json.writerWithDefaultPrettyPrinter().writeValueAsString(test));
    }
}
