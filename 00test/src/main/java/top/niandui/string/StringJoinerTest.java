package top.niandui.string;

import org.junit.Test;

import java.util.StringJoiner;

/**
 * StringJoiner 类测试
 */
public class StringJoinerTest {

    @Test
    public void test1() {
        StringJoiner sj = new StringJoiner(",", "[", "]");

        String[] strings = new String[3];
        strings[0] = "0000";
        strings[1] = "1111";
        strings[2] = "2222";

        for (String string : strings) {
            sj.add(string);
        }

        System.out.println(sj.toString());

    }
}
