package top.niandui.component;

import top.niandui.model.Info;
import top.niandui.utils.WebClientUtil;

import java.util.List;
import java.util.Scanner;

/**
 * @Title: BiQuGeCOM.java
 * @description: BiQuGeCOM
 * @time: 2020/1/19 11:00
 * @author: liyongda
 * @version: 1.0
 */
public class BiQuGeCOM {

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        try {
            /*
            * 联盟一姐的生活手册.txt http://www.biqige.com/107_107926/35015270.html
            * 腾飞我的航空时代.txt https://www.biduo.cc/biquge/53_53393/c22077441.html
            */
            Scanner sc = new Scanner(System.in);
            System.out.println("http://www.biqige.com");
            System.out.print("请输入小说第一章地址：");
            String url = sc.nextLine();
            System.out.print("请输入小说名称：");
            String fileName = sc.nextLine();
            if (!fileName.endsWith(".txt")) {
                fileName += ".txt";
            }
            System.out.println("获取文本...");
            List<String> list = WebClientUtil.getContext(getInfo(url));
            System.out.println("获取完毕");
            System.out.println("保存文本...");
            WebClientUtil.saveFile(fileName, list);
            System.out.println("保存完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Info getInfo(String startUrl) {
        Info info = new Info();
        info.startUrl = startUrl;
        info.titleXPathExpr = "//div[@class='bookname']/h1/text()";
        info.contentXPathExpr = "//div[@id='content']/text()";
        info.contentEndIndexOffset = -3;
        info.anchorXPathExpr = "//div[@class='bottem2']//a";
        info.nextAnchorIndex = 3;
        return info;
    }
}
