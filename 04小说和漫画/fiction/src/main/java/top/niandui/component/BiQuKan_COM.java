package top.niandui.component;

import top.niandui.model.Info;
import top.niandui.utils.WebClientUtil;

/**
 * @Title: BiQuKan_COM.java
 * @description: BiQuKan_COM
 * @time: 2020/1/19 11:19
 * @author: liyongda
 * @version: 1.0
 */
public class BiQuKan_COM {

    public static void main(String[] args) {
        /*
         * 腾飞我的航空时代.txt https://www.biqukan.com/72_72762/491732466.html
         */
        System.out.println("http://www.biqukan.com");
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
        info.titleXPathExpr = "//div[@class='content']/h1/text()";
        info.customizeTitleHandler();
        info.contentXPathExpr = "//div[@id='content']/text()";
        info.contentEndIndexOffset = -3;
        info.anchorXPathExpr = "//div[@class='page_chapter']//a";
        info.nextAnchorIndex = 2;
        return info;
    }
}
