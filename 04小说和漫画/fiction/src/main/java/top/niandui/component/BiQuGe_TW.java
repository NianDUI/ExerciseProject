package top.niandui.component;

import top.niandui.model.Info;
import top.niandui.utils.WebClientUtil;

/**
 * @Title: BiQuGe_TW.java
 * @description: BiQuGe_TW
 * @time: 2020/1/26 21:32
 * @author: liyongda
 * @version: 1.0
 */
public class BiQuGe_TW {

    public static void main(String[] args) {
        /*
         * 铜胎掐丝珐琅锻造锤.txt https://www.biquge.tw/488_488711/2548121.html
         */
        System.out.println("http://www.biquge.tw");
        start();
    }

    /**
     * 开始方法
     */
    public static void start() {
        WebClientUtil.importInfo(getInfo());
    }

    /**
     * 组装Info对象
     * @return
     */
    private static Info getInfo() {
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
