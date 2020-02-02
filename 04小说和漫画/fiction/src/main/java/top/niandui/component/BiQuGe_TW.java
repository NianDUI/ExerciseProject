package top.niandui.component;

import top.niandui.model.IBaseComponent;
import top.niandui.model.Info;

/**
 * @Title: BiQuGe_TW.java
 * @description: BiQuGe_TW
 * @time: 2020/1/26 21:32
 * @author: liyongda
 * @version: 1.0
 */
public class BiQuGe_TW implements IBaseComponent {

    public static void main(String[] args) {
        new BiQuGe_TW().start();
    }

    @Override
    public void startBeforePrint() {
        /*
         * 铜胎掐丝珐琅锻造锤.txt https://www.biquge.tw/488_488711/2548121.html
         */
        System.out.println("http://www.biquge.tw");
        System.out.println("http://www.e8zw.com");
    }

    @Override
    public Info getInfo() {
        Info info = new Info();
        info.titleXPathExpr = "//div[@class='bookname']/h1/text()";
        info.titleNewLine = "";
        info.customizeTitleHandler();
        info.contentXPathExpr = "//div[@id='content']/text()";
        info.anchorXPathExpr = "//div[@class='bottem2']//a";
        info.nextAnchorIndex = 2;
        return info;
    }
}
