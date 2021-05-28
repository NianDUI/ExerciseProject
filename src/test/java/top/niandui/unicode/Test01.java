package top.niandui.unicode;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UNICODE 测试01
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/5/26 11:44
 */
public class Test01 {


    @Test
    // \u8BC1\u4E66\u7684\u8DEF\u5F84 -> 证书的路径
    public void test01(String aa) {
        // server: \u8BC1\u4E66\u7684\u8DEF\u5F84.
        Pattern pattern = Pattern.compile("\\\\u[0-9A-Fa-f]{4}");

        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        String line;
        while (!"-1".equals((line = sc.nextLine()))) {
            sb.append(line).append("\n");
        }
        line = sb.toString();
        Matcher matcher = pattern.matcher(line);
        Set<String> set = new HashSet<>(matcher.groupCount());
        while (matcher.find()) {
            set.add(matcher.group());
        }
        for (String s : set) {
            line = line.replace(s, Character.toString((char) Integer.parseInt(s.replace("\\u", ""), 16)));
        }
        System.out.println(line);
    }
}
