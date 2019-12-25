package top.niandui.frame.utils;

import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;

/**
 * 文件操作工具
 */
public class FileUtils {
    private FileUtils() {}

    public static boolean write(String pathname, Object content) throws IOException {
        File file = new File(pathname);
        System.out.println(file.getAbsolutePath());
        if (file.getParentFile() != null && file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileWriter fw = new FileWriter(file);
        fw.write(content.toString());
        fw.flush();
        fw.close();
        return false;
    }

    public static String read(String pathname) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(pathname));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}
