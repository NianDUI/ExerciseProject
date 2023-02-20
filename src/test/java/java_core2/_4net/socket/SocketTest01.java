package java_core2._4net.socket;

import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Socket测试
 *
 * @author liyongda
 * @version 1.0
 * @date 2022/4/19 17:04
 */
public class SocketTest01 {

    @Test
    public void test_4_1_2() throws Exception {
        // time-a.nist.gov 129.6.15.28
        try (
                Socket socket = new Socket("129.6.15.28", 13);
                Scanner sc = new Scanner(socket.getInputStream(), "UTF-8")
        ) {
            do {
                String line = sc.nextLine();
                System.out.println(line);
            } while (sc.hasNextLine());
        }
    }

    @Test
    public void test_4_1_3() throws Exception {
//        Socket socket = new Socket("129.6.15.28", 13);
//        // 设置超时
//        socket.setSoTimeout(1000);
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("129.6.15.28", 13), 1000);
        try (
                Scanner sc = new Scanner(socket.getInputStream(), "UTF-8")
        ) {
            do {
                String line = sc.nextLine();
                System.out.println(line);
            } while (sc.hasNextLine());
        }
        socket.close();
    }
}
