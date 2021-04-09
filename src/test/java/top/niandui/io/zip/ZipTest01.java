package top.niandui.io.zip;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * zip测试
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/3/26 15:32
 */
public class ZipTest01 {
    ObjectMapper json = new ObjectMapper();

    @Test
    public void test01() throws Exception {
        ZipFile zf = new ZipFile("d:/test.zip", Charset.forName("GBK"));
        Enumeration<? extends ZipEntry> entries = zf.entries();
        while (entries.hasMoreElements()) {
            ZipEntry ze = entries.nextElement();
//            InputStream is = zf.getInputStream(ze);
            System.out.println(ze);
        }
        System.out.println(zf.getComment());
        zf.close();
    }

    @Test
    public void test02() throws Exception {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("d:/test2.zip"));
        ZipEntry e = new ZipEntry("aaa/测试.txt");
        zos.putNextEntry(e);
        Files.copy(Paths.get("d:/test.txt"), zos);
        e = new ZipEntry("aaa/测试2.txt");
        zos.putNextEntry(e);
        Files.copy(Paths.get("d:/test2.txt"), zos);
        zos.close();
    }

}
