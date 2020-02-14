package top.niandui.component;

import top.niandui.model.IBaseComponent;
import top.niandui.model.Info;
import top.niandui.utils.PrintUtil;

/**
 * @Title: BiQuGe_TW.java
 * @description: BiQuGe_TW
 * @time: 2020/1/26 21:32
 * @author: liyongda
 * @version: 1.0
 */
public class BiQuGg_TW implements IBaseComponent {

    public static void main(String[] args) {
        new BiQuGg_TW().start();
    }

    @Override
    public void startBeforePrint() {
        /*
         * 重生之互联网霸主.txt https://www.biqugg.com/xs/15480/10497668.html
         */
        PrintUtil.println("\thttp://www.biqugg.com");
    }

    @Override
    public Info getInfo() {
        Info info = new Info();
        info.titleXPathExpr = "//div[@class='bookname']/h1/text()";
        info.customizeTitleHandler();
        info.contentXPathExpr = "//div[@id='content']/text()";
        info.anchorXPathExpr = "//div[@class='bottem2']//a";
        info.nextAnchorIndex = 2;
        return info;
    }
}
