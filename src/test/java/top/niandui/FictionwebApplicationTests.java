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
    public void navicatSqlHandleUtil() throws Exception {
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

    @Test
    public void cmdSqlHandleUtil() throws Exception {
        // # 数据库备份和还原命令
        // # pg_dump -h 192.168.3.10 -p 5432 -U postgres -c -d fiction -f /soft/db/fiction2021
        // # psql -h 127.0.0.1 -p 5432 -U postgres -d fiction -f /soft/db/fiction2021
        // # 备份和还原数据库全部信息
        // # pg_dumpall -h 127.0.0.1 -p 5432 -U postgres -c -f /soft/db/db_bak.sql
        // # psql -h 127.0.0.1 -p 5432 -U postgres -f /soft/db/db_bak.sql
        Scanner sc = new Scanner(Paths.get("d:/fiction20210620"), "UTF-8");
        PrintStream ps = new PrintStream("d:/fiction_cmd2.sql");
        boolean skip = false;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (skip) {
                // 结束跳过copy数据
                if ("\\.".equals(line)) {
                    skip = false;
                }
                // 结束跳过创建子表
                if (line.startsWith("ALTER TABLE public.paragraph_")) {
                    skip = false;
                }
                continue;
            }
            // 跳过copy数据
            if (!skip && line.startsWith("COPY public.chapter (")) {
                skip = true;
                continue;
            }
            if (!skip && line.startsWith("COPY public.paragraph (")) {
                skip = true;
                continue;
            }
            if (!skip && line.startsWith("COPY public.paragraph_")) {
                skip = true;
                continue;
            }
            // 跳过创建子表
            if (!skip && line.startsWith("CREATE TABLE public.paragraph_")) {
                skip = true;
                continue;
            }
            // 跳过包含paragraph_的
            if (!skip && line.contains("paragraph_")) {
                continue;
            }
            ps.println(line);
        }
        sc.close();
        ps.close();
    }
}
