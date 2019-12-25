package top.niandui.systemtest;

/**
 * java.lang.System 测试
 */
public class SystemTest {
    public static void main(String[] args) {
        System.getProperties().forEach((o1, o2) -> System.out.println(o1 + ": " + o2));

        System.out.println();
        System.out.println(System.getProperty("user.dir"));
    }
}
