package top.niandui;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * 端口占用
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/7/15 14:33
 */
public class PortOccupy {
    public static void main(String[] args) {
        int[] ports = {5431, 5555, 5432, 5433, 5434, 5436, 9129, 9073, 9066, 9000, 9999, 9001, 9007};
        for (int port : ports) {
            try (ServerSocket ss = new ServerSocket(port)) {
            } catch (IOException e) {
                System.out.println("端口：" + port + "\t" + e.getMessage());
            }
        }

    }
}
