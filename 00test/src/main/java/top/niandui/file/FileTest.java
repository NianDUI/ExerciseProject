package top.niandui.file;

import org.apache.commons.net.nntp.NNTP;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * 文件测试
 */
public class FileTest {
    @Test
    public void test1() throws Exception {
        String content = readFile(getFilePath("aaa.zip"));
        System.out.println(content);
        downLoadZip(getFilePath("bbb.zip"), content);

    }

    public static String readFile(File file) throws Exception {
        StringBuilder sb = new StringBuilder();
        InputStream isr = new FileInputStream(file);
        byte[] bytes = new byte[1024];
        int l = 0;
        while ((l = isr.read(bytes)) > 0) {
            sb.append(new String(new String(bytes, 0, l).getBytes(Charset.defaultCharset())));
        }
        return sb.toString();
    }

    public static void downLoadZip(File file, String content) {
        OutputStream buff = null;
        try {
            buff = new FileOutputStream(file);
            buff.write(content.getBytes(Charset.defaultCharset()));
            buff.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (buff != null) {
                    buff.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public static File getFilePath(String fileName) {
        String pathStr = ClassLoader.getSystemResource("").getPath();
        File path = new File(pathStr).getParentFile();
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path, fileName);
        return file;
    }
}
