package top.niandui.component;

import top.niandui.model.Info;
import top.niandui.utils.WebClientUtil;

/**
 * @Title: BiQuGe7_COM.java
 * @description: BiQuGe7_COM
 * @time: 2020/1/28 15:47
 * @author: liyongda
 * @version: 1.0
 */
public class BiQuGe7_COM {

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        /*
         * 科学大佬的文艺生活.txt http://www.biquge7.com/book/5568014/520207084.html
         */
        System.out.println("http://www.biquge7.com");
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
        info.contentEndIndexOffset = -1;
        info.anchorXPathExpr = "//div[@class='page_chapter']//a";
        info.nextAnchorIndex = 2;
        return info;
    }
}
