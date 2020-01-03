package top.niandui.string;

import org.junit.Test;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * String 的 join 处理测试
 */
public class StringJoinTest {
    @Test
    public void test1() {

        String[] strings = new String[3];
        strings[0] = "0000";
        strings[1] = "1111";
        strings[2] = "2222";

        // 1 自己处理

        // 2 String类的join方法处理
        System.out.println(String.join(",", strings));

        // 3 StringJoiner 类进行处理
        StringJoiner sj = new StringJoiner(",", "[", "]");
        for (String string : strings) {
            sj.add(string);
        }
        System.out.println("sj = " + sj.toString());

        // 4 Stream 流 处理
        String collect = Arrays.stream(strings).collect(Collectors.joining(",", "[", "]"));
        System.out.println("collect = " + collect);
    }
}
