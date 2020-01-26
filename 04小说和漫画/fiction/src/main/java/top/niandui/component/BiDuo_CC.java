package top.niandui.component;

import top.niandui.model.Info;
import top.niandui.utils.WebClientUtil;

import java.util.List;
import java.util.Scanner;

/**
 * @Title: BiDuo_CC.java
 * @description: BiDuo_CC
 * @time: 2020/1/19 11:00
 * @author: liyongda
 * @version: 1.0
 */
public class BiDuo_CC {

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        /*
         * 腾飞我的航空时代.txt https://www.biduo.cc/biquge/53_53393/c22077441.html
         */
        System.out.println("http://www.biduo.cc");
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
        info.nextAnchorIndex = 2;
        return info;
    }
}
