package top.niandui.uuid;

import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.UUID;

/**
 * UUID 测试
 */
public class UUIDTest {
    @Test
    public void test01() throws Exception {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        UUID uuid1 = UUID.nameUUIDFromBytes("mwq".getBytes());
        System.out.println(uuid1);
        UUID uuid2 = UUID.fromString(uuid1.toString());
        System.out.println(uuid1.hashCode());
        System.out.println(uuid1.toString());
        System.out.println(uuid1.version());
        System.out.println(uuid1.compareTo(uuid2));
        System.out.println(uuid1.equals(uuid2));
        System.out.println(uuid1.getLeastSignificantBits());
        System.out.println(uuid1.getMostSignificantBits());
        System.out.println(uuid1.variant());
//        System.out.println(uuid.clockSequence());
//        System.out.println(uuid1.node());
//        System.out.println(uuid1.timestamp());

        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[16];
        for (int i = 0; i < 10; i++) {
            secureRandom.nextBytes(bytes);
            System.out.println(Arrays.toString(bytes));
            System.out.println(new String(bytes));
        }
    }
}
