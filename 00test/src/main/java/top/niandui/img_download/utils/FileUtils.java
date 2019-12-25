package top.niandui.img_download.utils;

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
        FileWriter fw = new FileWriter(pathname);
        fw.write(content.toString());
        fw.flush();
        fw.close();
        return false;
    }

    public static void write(String url, String... pathname) throws IOException {
        File file = new File(Paths.get("", pathname).toString());
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        InputStream inputStream = new URL(url).openStream();
        BufferedInputStream bin = new BufferedInputStream(inputStream);
        byte[] bytes = new byte[64];
        int l;
        while ((l = bin.read(bytes)) > 0) {
            bos.write(bytes, 0, l);
        }
        bos.flush();
        bos.close();
        bin.close();

//        WebClient webClient = WebClientUtils.getWebClient();
//        webClient.get
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

    public static void main(String[] args) throws IOException {
        write("https://goss.veer.com/creative/vcg/veer/1600water/veer-164919552.jpg", "1.jpg");
//        BufferedImage bimg = ImageIO.read(new URL("https://****.com/***/**/1235/01.jpg"));
//        WebClient webClient = WebClientUtils.getWebClient();
    }

    @Test
    public void text1() {

    }
}
