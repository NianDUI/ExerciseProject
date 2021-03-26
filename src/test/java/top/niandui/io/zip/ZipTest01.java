package top.niandui.io.zip;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
        ZipFile zf = new ZipFile("d:/test.zip");
        Enumeration<? extends ZipEntry> entries = zf.entries();
        while (entries.hasMoreElements()) {
            ZipEntry ze = entries.nextElement();
//            InputStream is = zf.getInputStream(ze);
            System.out.println(ze);
        }
        System.out.println(zf.getComment());
        zf.close();
    }


}
