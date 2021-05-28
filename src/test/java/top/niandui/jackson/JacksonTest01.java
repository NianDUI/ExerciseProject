package top.niandui.jackson;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Jackson ObjectMapper 测试
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/5/28 9:46
 */
public class JacksonTest01 {
    // json处理对象
    private final static ObjectMapper json = new ObjectMapper();

    static {
        // 反序列化：关闭未知属性失败
        json.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 序列化：字符串转json去除值为null的属性
        json.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

        // 设置当前包日志打印机级别
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger(JacksonTest01.class);
        logger.setLevel(Level.DEBUG);
    }

    @Test
    public void test01() throws Exception {
        // 反序列化：关闭未知属性失败
        String a = "{\n" +
                "  \"f_trailing\": 0,\n" +
                "  \"f_serviceid\": 3750,\n" +
                "  \"f_body\": {\n" +
                "    \"f_method\": \"POST\"\n" +
                "  }\n" +
                "}";

        A a1 = json.readValue(a, A.class);
        System.out.println(a1);

        System.out.println(json.writeValueAsString(LocalDateTime.now()));

        System.out.println();
    }

    @Test
    public void test02() throws Exception {
        // 使用构建器起构建，参考 JacksonAutoConfiguration 和 JacksonProperties
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        ObjectMapper mapper = builder.createXmlMapper(false).build();

        System.out.println(mapper.writeValueAsString(LocalDateTime.now()));


    }


    @Data
    public static class A {
//        private int f_trailing;
        private int f_serviceid;
        private Map f_body;
    }
}
