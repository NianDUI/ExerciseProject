package top.niandui.io.object;

import org.junit.jupiter.api.Test;

import java.io.*;

/**
 * 对象流测试
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/5/14 13:58
 */
public class ObjectTest01 implements Serializable {

    @Test
    public void test01() throws Exception {
        // 2.4.1
        String path = "d:/ztest/objecttest";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            A a = new A();
            a.name = "a";
            B b1 = new B();
            b1.name = "b1";
            b1.a = a;
            B b2 = new B();
            b2.name = "b1";
            b2.a = a;
            oos.writeObject(new Object[]{b1, a, b2});
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            Object[] o = (Object[]) ois.readObject();
            System.out.println();
        }
    }


    public class A implements Serializable {
        String name;
        // 瞬时的，序列化是不会被序列化
        transient String end = "A.end";
    }

    public class B implements Serializable {
        String name;
        A a;
    }
}
