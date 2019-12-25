import utils.BiQuGe;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class BiQuGeApplication {

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        try {
            /*请
            * 输入笔趣阁小说第一章地址：http://www.biqige.com/107_107926/35015270.html
            * 请输入笔趣阁小说名称：联盟一姐的生活手册.txt
            *
            * 腾飞我的航空时代 https://www.biduo.cc/biquge/53_53393/c22077441.html
            */
            Scanner sc = new Scanner(System.in);
            System.out.println("http://www.biqige.com");
            System.out.print("请输入笔趣阁小说第一章地址：");
            String url = sc.nextLine();
            System.out.print("请输入笔趣阁小说名称：");
            String fileName = sc.nextLine();
            // "https://www.biquge.lu/book/59471/488362450.html"
            System.out.println("获取文本...");
            List<String> list = BiQuGe.getContext(url);
            System.out.println("获取完毕");
            System.out.println("保存文本...");
            saveFile(fileName, list);
            System.out.println("保存完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(String fileName, List<String> list) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8));
            for (String str : list) {
                bw.write(str);
            }
            bw.flush();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
