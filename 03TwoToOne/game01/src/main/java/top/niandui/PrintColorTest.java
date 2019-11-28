package top.niandui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * 控制台输出颜色字体的测试类
 *  https://blog.csdn.net/hanghang121/article/details/40825527
 */
public class PrintColorTest {
    public static void main(String[] args) {
        System.out.println("\033[30;0m" + "0 好好学习" + "\033[0m");
        System.out.println("\033[31;0m" + "1 好好学习" + "\033[0m");
        System.out.println("\033[32;0m" + "2 好好学习" + "\033[0m");
        System.out.println("\033[33;0m" + "3 好好学习" + "\033[0m");
        System.out.println("\033[34;0m" + "4 好好学习" + "\033[0m");
        System.out.println("\033[35;0m" + "5 好好学习" + "\033[0m");
        System.out.println("\033[36;0m" + "6 好好学习" + "\033[0m");
        System.out.println("\033[37;0m" + "7 好好学习" + "\033[0m");

        System.out.println("\033[37;7m" + "7 好好学习" + " \033[0m");

        System.out.println("\033[1;31;42m" + "77 好好学习" + "\033[0m");
        //30-37有颜色
        System.out.println("\033[38;0m" + "8 好好学习" + "\033[0m");
        System.out.println("直接：好好学习");
//        for (int i = 0; i < 100; i++) {
//            System.out.println("\033[" + i + ";0m" + i + " 好好学习" + "\033[0m");
//        }
//        ArrayList<Object> collect = Stream.generate(Math::random).limit(5).map(i -> i * 10).collect(ArrayList::new, (objects, aDouble) -> objects.add(aDouble), (objects, objects2) -> objects.addAll(objects2));
//        System.out.println(collect);
//        Stream.iterate(1, i -> i+1).limit(5).forEach(System.out::println);
    }

    static class A {
        List<String> list;

        public static A getInstance() {
            return new A();
        }

        public A add(String... str) {
            Collections.addAll(list, str);
            return this;
        }
    }
}


