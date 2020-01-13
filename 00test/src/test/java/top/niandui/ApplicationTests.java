package top.niandui;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.niandui.object_bytearray.Test01;
import top.niandui.utils.RedisUtilByte;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class ApplicationTests {

    @Autowired
    RedisUtilByte redisUtilByte;

    @Test
    void contextLoads() {
    }

    @Test
    void object_bytearray() throws Exception {
        Test01 test01 = new Test01();
        Map<String, byte[]> map = new HashMap<>();
        map.put("111", test01.toStringByteArray("111"));
        map.put("222", test01.toStringByteArray(222));
        map.put("333", test01.toStringByteArray(333.33));
        map.put("444", test01.toStringByteArray(true));
        redisUtilByte.hmSet("test", map);
    }

}
