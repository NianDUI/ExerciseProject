package top.niandui;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @Title: SSHJTest.java
 * @description: SSHJTest
 * @time: 2020/9/28 11:22
 * @author: liyongda
 * @version: 1.0
 */
@Slf4j
public class SSHJTest {
    public static void main(String[] args) throws Exception {
        setLogLevel();
        sshj();
    }

    public static void setLogLevel() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = context.getLogger("root");
        root.setLevel(Level.ERROR);
    }

    public static void sshj() throws Exception {
        SSHClient ssh = new SSHClient();
//        ssh.loadKnownHosts();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect("**", 9220);
//        ssh.authPassword("root", "password");
        ssh.authPublickey("root", System.getProperty("user.home") + "/.ssh/id_rsa");
        ssh.setRemoteCharset(StandardCharsets.UTF_8);
        try (Session session = ssh.startSession()) {
            try (Session.Shell shell = session.startShell()) {
                startOutput(shell.getInputStream());
                startOutput(shell.getErrorStream());
                OutputStream os = shell.getOutputStream();
                Scanner sc = new Scanner(System.in);
                String line;
                do {
                    line = sc.nextLine() + "\n";
                    os.write(line.getBytes());
                    os.flush();
                } while (!"exit\n".equals(line));
            }
        }
    }

    public static void startOutput(InputStream is) {
        new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
