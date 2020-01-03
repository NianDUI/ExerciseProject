package top.niandui.regex;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则测试
 */
public class RegexTest {

    @Test
    public void test1() {
        Pattern pattern = Pattern.compile("(Abc)|(XYZ)");
        Matcher matcher = pattern.matcher("XYZAbcAbcAbcXYZAbcAb");
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }
}
