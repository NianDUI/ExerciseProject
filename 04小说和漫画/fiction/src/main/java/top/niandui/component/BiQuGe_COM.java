package top.niandui.component;

import top.niandui.model.Info;
import top.niandui.utils.WebClientUtil;

import java.util.List;
import java.util.Scanner;

/**
 * @Title: BiQuGe_COM.java
 * @description: BiQuGe_COM
 * @time: 2020/1/19 11:00
 * @author: liyongda
 * @version: 1.0
 */
public class BiQuGe_COM {

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        /*
         * 联盟一姐的生活手册.txt http://www.biqige.com/107_107926/35015270.html
         */
        System.out.println("http://www.biqige.com");
        WebClientUtil.importInfo(getInfo());
    }

    /**
     * 组装Info对象
     * @return
     */
    private static Info getInfo() {
        Info info = new Info();
        info.titleXPathExpr = "//div[@class='bookname']/h1/text()";
        info.customizeTitleHandler();
        info.contentXPathExpr = "//div[@id='content']/text()";
        info.contentEndIndexOffset = -3;
        info.anchorXPathExpr = "//div[@class='bottem2']//a";
        info.nextAnchorIndex = 3;
        return info;
    }
}
