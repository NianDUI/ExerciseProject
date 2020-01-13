package top.niandui.object_bytearray;

import org.junit.Test;

import java.io.*;

/**
 * @Title: Test.java
 * @description: 对象和byte数组之间的转化
 * @time: 2020/1/9 17:54
 * @author: liyongda
 * @version: 1.0
 */
public class Test01 {

    @Test
    public void main() {

    }

    public byte[] toByteArray(Object o) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(o);
        oos.flush();
        byte[] bytes = bos.toByteArray();
        oos.close();
        return bytes;
    }

    public <T> T toObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        T t = (T) ois.readObject();
        ois.close();
        return t;
    }

    public byte[] toStringByteArray(Object o) {
        return o.toString().getBytes();
    }
}
