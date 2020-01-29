package top.niandui.component;

import top.niandui.model.Info;
import top.niandui.utils.WebClientUtil;

/**
 * @Title: BQG5_CC.java
 * @description: BQG5_CC
 * @time: 2020/1/29 12:39
 * @author: liyongda
 * @version: 1.0
 */
public class BQG5_CC {

    public static void main(String[] args) {
        /*
         * 信息空间.txt https://www.bqg5.cc/128_128660/171885147.html
         */
        System.out.println("http://www.bqg5.cc");
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
        info.customizeTitleHandler();
        info.contentXPathExpr = "//div[@id='content']/p/text()";
        info.anchorXPathExpr = "//div[@class='bottem2']//a";
        info.nextAnchorIndex = 3;
        return info; // dump
    }
}
