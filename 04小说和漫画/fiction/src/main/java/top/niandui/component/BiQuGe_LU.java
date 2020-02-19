package top.niandui.component;

import top.niandui.model.IBaseComponent;
import top.niandui.model.Info;
import top.niandui.utils.PrintUtil;

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
        PrintUtil.println("\thttp://www.biquge.lu");
        PrintUtil.println("\thttp://www.biqumo.com");
        PrintUtil.println("\thttp://www.bqg99.cc");
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
        info.customizeWriteType();
        return info;
    }
}
