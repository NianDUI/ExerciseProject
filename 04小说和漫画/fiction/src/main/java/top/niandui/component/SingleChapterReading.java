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
    }

    @Override
    public Info getInfo() {
        Scanner sc = new Scanner(System.in);
        Info info = new Info();
        info.startUrl = "http://www.mcmssc.com/90_90956/38213658.html";
        info.fileName = "aa";
        info.titleXPathExpr = "//div[@class='bookname']/h1/text()";
        info.contentXPathExpr = "//div[@id='content']/text()";
        info.anchorXPathExpr = "//div[@class='bottem2']//a";
        info.nextAnchorIndex = 3;
        info.isEndHref = (nextPageUrl, content) -> {
            WebClientUtil.saveFile(info.fileName, Arrays.asList(content.toString()), info.isAppendSave);
            if (nextPageUrl.toLowerCase().contains("html")) {
                System.out.print("是否获取下一章(1获取)：");
                String line = sc.nextLine();
                if ("1".equals(line.trim())) {
                    return false;
                }
            }
            return true;
        };
        info.isSaveFile = false;
        return info;
    }
}
