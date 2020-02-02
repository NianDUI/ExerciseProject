package top.niandui.component;

import top.niandui.model.IBaseComponent;
import top.niandui.model.Info;

/**
 * @Title: BiQuGe_LU.java
 * @description: BiQuGe_LU
 * @time: 2020/1/19 11:16
 * @author: liyongda
 * @version: 1.0
 */
public class BiQuGe_LU implements IBaseComponent {

    public static void main(String[] args) {
        new BiQuGe_LU().start();
    }

    @Override
    public void startBeforePrint() {
        /*
         * 魔法纪元黎明.txt https://www.biquge.lu/book/59471/488362450.html
         */
        System.out.println("http://www.biquge.lu");
        System.out.println("http://www.biqumo.com");
    }

    @Override
    public Info getInfo() {
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
