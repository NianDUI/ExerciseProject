import utils.BiQuGe;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class BiQuGeApplication {

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("https://www.biquge.lu");
            System.out.print("请输入笔趣阁小说第一章地址：");
            String url = sc.nextLine();
            System.out.print("请输入笔趣阁小说名称：");
            String fileName = sc.nextLine();
            // "https://www.biquge.lu/book/59471/488362450.html"
            System.out.println("获取文本...");
            List<String> list = BiQuGe.getContext(url);
            System.out.println("获取完毕");
            System.out.println("保存文本...");
            BiQuGe.saveFile(fileName, list);
            System.out.println("保存完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
