package top.niandui;

import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.Scanner;

//@SpringBootTest
class FictionwebApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void sqlHandleUtil() throws Exception {
        Scanner sc = new Scanner(Paths.get("d:/public.sql"), "UTF-8");
        PrintStream ps = new PrintStream("d:/fiction.sql");
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("INSERT INTO \"public\".\"chapter\" VALUES")) {
                continue;
            }
            if (line.startsWith("INSERT INTO \"public\".\"paragraph\" VALUES")) {
                continue;
            }
            if (line.matches("INSERT INTO \"public\".\"paragraph_.*\" VALUES.*")) {
                continue;
            }
            ps.println(line);
        }
        sc.close();
        ps.close();
    }
}
