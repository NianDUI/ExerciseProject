package top.niandui.component;

import top.niandui.model.IBaseComponent;
import top.niandui.model.Info;
import top.niandui.utils.PrintUtil;

/**
 * @Title: BiQuGexx_COM.java
 * @description: BiQuGexx_COM
 * @time: 2020/1/19 11:22
 * @author: liyongda
 * @version: 1.0
 */
public class BiQuGexx_COM implements IBaseComponent {

    public static void main(String[] args) {
        new BiQuGexx_COM().start();
    }

    @Override
    public void startBeforePrint() {
        /*
         * 科技霸权.txt https://www.biqugexx.com/70_70265/16165726.html
         */
        PrintUtil.println("\thttp://www.biqugexx.com");
    }

    @Override
    public Info getInfo() {
        Info info = new Info();
        info.titleXPathExpr = "//div[@class='bookname']/h1/text()";
        info.titleNewLine = "";
        info.customizeTitleHandler();
        info.contentXPathExpr = "//div[@id='content']/text()";
        info.anchorXPathExpr = "//div[@class='bottem2']//a";
        info.nextAnchorIndex = 3;
        return info;
    }
}
