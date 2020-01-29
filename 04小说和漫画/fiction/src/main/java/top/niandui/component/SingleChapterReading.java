package top.niandui.component;

import top.niandui.model.IBaseComponent;
import top.niandui.model.Info;
import top.niandui.utils.WebClientUtil;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @Title: SingleChapterReading.java
 * @description: 单章阅读
 * @time: 2020/1/20 14:55
 * @author: liyongda
 * @version: 1.0
 */
public class SingleChapterReading implements IBaseComponent {

    public static void main(String[] args) {
        new SingleChapterReading().start();
    }

    @Override
    public void startBeforePrint() {
        /*
         * 巫师不朽.txt http://www.booktxt.net/6_6254/3144372.html 324章
         */
        System.out.println("http://www.booktxt.net");
    }

    @Override
    public Info getInfo() {
        Scanner sc = new Scanner(System.in);
        Info info = new Info();
        System.out.print("请输入章节地址：");
        info.startUrl = sc.nextLine();
        info.titleXPathExpr = "//div[@class='bookname']/h1/text()";
        info.customizeTitleHandler();
        info.contentXPathExpr = "//div[@id='content']/text()";
        info.anchorXPathExpr = "//div[@class='bottem2']//a";
        info.nextAnchorIndex = 3;
        info.isEndHref = (nextPageUrl, content) -> {
//        WebClientUtil.saveFile(content.substring(0, content.indexOf("\r\n")) + ".txt",
//                Arrays.asList(content.toString()));
            WebClientUtil.saveFile("temp.txt", Arrays.asList(content.toString()));
            if (nextPageUrl.toLowerCase().contains("html")) {
                System.out.print("是否获取下一章(1获取)：");
                String line = sc.nextLine();
                if ("1".equals(line.trim())) {
                    return false;
                }
            }
            return true;
        };
        return info;
    }
}
