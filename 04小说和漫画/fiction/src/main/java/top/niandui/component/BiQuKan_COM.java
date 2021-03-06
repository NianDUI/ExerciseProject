package top.niandui.component;

import top.niandui.model.IBaseComponent;
import top.niandui.model.Info;
import top.niandui.utils.PrintUtil;

/**
 * @Title: BiQuKan_COM.java
 * @description: BiQuKan_COM
 * @time: 2020/1/19 11:19
 * @author: liyongda
 * @version: 1.0
 */
public class BiQuKan_COM implements IBaseComponent {

    public static void main(String[] args) {
        new BiQuKan_COM().start();
    }

    @Override
    public void startBeforePrint() {
        /*
         * 腾飞我的航空时代.txt https://www.biqukan.com/72_72762/491732466.html
         */
        PrintUtil.println("\thttp://www.biqukan.com");
        PrintUtil.println("\thttps://www.biqumo.com");
    }

    @Override
    public Info getInfo() {
        Info info = new Info();
        info.titleXPathExpr = "//div[@class='content']/h1/text()";
        info.customizeTitleHandler();
        info.contentXPathExpr = "//div[@id='content']/text()";
        info.contentEndIndexOffset = -2;
        info.anchorXPathExpr = "//div[@class='page_chapter']//a";
        info.nextAnchorIndex = 2;
        info.customizeWriteType();
        return info;
    }
}
