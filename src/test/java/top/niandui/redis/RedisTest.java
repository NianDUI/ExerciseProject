package top.niandui.redis;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
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
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("192.168.1.14");
        config.setPort(6379);
        // 64
        config.setPassword("QTEyMzEyQHhxIQ==");
        config.setDatabase(2);
//        LettuceConnectionFactory factory = new LettuceConnectionFactory(config);
        LettuceConnectionFactory factory = new LettuceConnectionFactory("127.0.0.1", 6379);
        factory.afterPropertiesSet();
        template.setConnectionFactory(factory);

        // 标准json序列化
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer(json);
        // key采用String的序列化方式
        template.setKeySerializer(RedisSerializer.string());
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(RedisSerializer.string());
        // value序列化方式采用 自定义byte数组的序列化方式
//        template.setValueSerializer(RedisSerializer.json());
        template.setValueSerializer(jsonRedisSerializer);
        // hash的value序列化方式采用 自定义byte数组的序列化方式
//        template.setHashValueSerializer(RedisSerializer.json());
        template.setHashValueSerializer(jsonRedisSerializer);
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
    public void testByte() throws Exception {
        redisUtil.set("test:json", 1);
        redisByteUtil.set("test:bytes", new byte[]{1});
        redisUtil.set("test:bytesjson", new byte[]{1});

        redisUtil.incr("test:json", 1);

        byte[] bytes = redisByteUtil.get("test:bytes");
        // null
        byte[] bytes2 = redisByteUtil.get("test:bytes2");

        System.out.println();
    }

    @Test
    public void test1() throws Exception {
        Set<Object> set = redisUtil.sGet("asdfasdf");
        System.out.println("set = " + set);
        // 成员不能为空
//        redisUtil.sAdd("set1");
        // 空指针异常
//        redisUtil.sAdd("set2", null);
        redisUtil.sAdd("set3", new String[]{null});
        redisUtil.sAdd("set4", new String[]{"", "123"});
    }

    @Test
    public void testObject() throws Exception {
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
        redisUtil.hSet("test", "1", map);
        map.put("a", 2);
        redisUtil.hSet("test", "1", map);

        redisUtil.hSet("test", "3", "asdf");
        redisUtil.hSet("test", "4", 123);

        Object test = redisUtil.hGet("test", "1");
        Object test3 = redisUtil.hGet("test", "3");
        Object test4 = redisUtil.hGet("test", "4");
        System.out.println(json.writerWithDefaultPrettyPrinter().writeValueAsString(test));
    }

    @Test
    public void testSet() {
        // 测试set类型
        String[] ss = {"O28", "4x7", "3htvaA", "oP9AAq", "M3qEK7"};
        long l = redisUtil.sAdd("test:set", 600, ss);
        Set<Object> objects = redisUtil.sGet("test:set");
        Object[] values = {objects};
        long l2 = redisUtil.sAdd("test:set2", 600, values);
        Set<Object> objects2 = redisUtil.sGet("test:set2");
        System.out.println(objects2);
    }
}
