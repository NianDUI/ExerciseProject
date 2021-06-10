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
        // 瞬时的，序列化是不会被序列化。但是可以通过 readObject 和 writeObject 方法实现
        transient String end = "B.end";

        // 替换默认序列化机制的读写对象方法
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            // 默认写当前对象的方法
            in.defaultReadObject();
            end = in.readUTF();
        }

        private void writeObject(ObjectOutputStream out) throws IOException, ClassNotFoundException {
            // 默认读当前对象的方法
            out.defaultWriteObject();
            out.writeUTF(end);
        }
    }

    public class C implements Serializable, Externalizable {
        String name;
        // 瞬时的，序列化是不会被序列化
        transient String end = "A.end";

        // 除了让序列化机制来保存和恢复对象数据，类还可以定义它自己的机制。
        // 为了做到这一点，这个类必须实现 Externalizable 接口，这需要它定义两个方法：
        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeUTF(name);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            name = in.readUTF();
        }
    }
}
