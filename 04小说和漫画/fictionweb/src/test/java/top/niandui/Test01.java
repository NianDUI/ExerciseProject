package top.niandui;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.util.Objects;

/**
 * @Title: Test01.java
 * @description: Test
 * @time: 2020/9/14 9:55
 * @author: liyongda
 * @version: 1.0
 */
@Slf4j
public class Test01 {
    // json处理对象
    public final static ObjectMapper json = new ObjectMapper();

    @Test
    public void md5Test() throws Exception {
        String str = "H4sIAAAAAAAAAAEoANf/6L+Z5piv5LiA5Liq5rWL6K+V5a2X56ym5LiyYWFhYmJiY2NjZGVmemcshV4oAAAA";
        System.out.println(Objects.hash(str));

        System.out.println(DigestUtils.md5DigestAsHex(str.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex(str.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex("".getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex("".getBytes()));
    }
}
