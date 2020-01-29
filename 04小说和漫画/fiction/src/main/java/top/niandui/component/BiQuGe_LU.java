package top.niandui.component;

import top.niandui.model.Info;
import top.niandui.utils.WebClientUtil;

/**
 * @Title: BiQuGe_LU.java
 * @description: BiQuGe_LU
 * @time: 2020/1/19 11:16
 * @author: liyongda
 * @version: 1.0
 */
public class BiQuGe_LU {

    public static void main(String[] args) {
        /*
         * 魔法纪元黎明.txt https://www.biquge.lu/book/59471/488362450.html
         */
        System.out.println("http://www.biquge.lu");
        start();
    }

    public static void start() {
        WebClientUtil.importInfo(getInfo());
    }

    /**
     * 组装Info对象
     * @return
     */
    private static Info getInfo() {
        Info info = new Info();
        info.titleXPathExpr = "//div[@id='content']/../h1/text()";
        info.customizeTitleHandler();
        info.contentXPathExpr = "//div[@id='content']/text()";
        info.contentEndIndexOffset = -2;
        info.anchorXPathExpr = "//div[@class='page_chapter']//a";
        info.nextAnchorIndex = 2;
        return info;
    }
}
