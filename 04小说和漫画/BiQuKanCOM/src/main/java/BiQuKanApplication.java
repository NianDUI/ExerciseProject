import utils.BiQuKan;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class BiQuKanApplication {

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        try {
            /*
             * 请输入笔趣阁小说第一章地址：https://www.biqukan.com/72_72762/491732466.html
             * 请输入笔趣阁小说名称：腾飞我的航空时代.txt
             */
            Scanner sc = new Scanner(System.in);
            System.out.println("http://www.biqukan.com");
            System.out.print("请输入笔趣看小说第一章地址：");
            String url = sc.nextLine();
            System.out.print("请输入笔趣看小说名称：");
            String fileName = sc.nextLine();
            // "https://www.biqukan.com/72_72762/491732466.html"
            System.out.println("获取文本...");
            List<String> list = BiQuKan.getContext(url);
            System.out.println("获取完毕");
            System.out.println("保存文本...");
            BiQuKan.saveFile(fileName, list);
            System.out.println("保存完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
