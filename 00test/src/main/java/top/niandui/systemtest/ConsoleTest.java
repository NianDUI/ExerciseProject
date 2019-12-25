package top.niandui.systemtest;

import java.io.Console;
import java.util.Formatter;
import java.util.Scanner;

/**
 * java.io.Console 测试
 */
public class ConsoleTest {
    public static void main(String[] args) {
        System.out.println(System.in);
        Console console = System.console();
        System.out.println(console); // null
//        console.format("%.2d", 50);

        Formatter formatter = new Formatter();
        formatter.format("%3f", 50.123f);
        System.out.println(formatter);
    }
}
