package top.niandui.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.niandui.utils.RedisUtil;

import java.util.Map;

/**
 * @Title: RedisTest.java
 * @description: RedisTest
 * @time: 2020/1/13 16:06
 * @author: liyongda
 * @version: 1.0
 */
@Component
public class RedisTest implements CommandLineRunner {
    @Autowired
    RedisUtil redisUtil;

    @Override
    public void run(String... args) throws Exception {
        Map userMap = (Map) redisUtil.get("cache_user:admin");
        Object f_regionid = userMap.get("f_regionid");
        System.out.println(f_regionid);
    }
}
