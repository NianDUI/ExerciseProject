package top.niandui.component;

import top.niandui.model.Info;
import top.niandui.utils.WebClientUtil;

/**
 * @Title: MCMSSC_COM.java
 * @description: MCMSSC_COM
 * @time: 2020/1/19 11:22
 * @author: liyongda
 * @version: 1.0
 */
public class MCMSSC_COM {

    public static void main(String[] args) {
        start();
    }

    /**
     * 开始方法
     */
    public static void start() {
        /*
         * 我的女友是声优.txt http://www.mcmssc.com/90_90956/38213658.html
         */
        System.out.println("http://www.mcmssc.com");
        WebClientUtil.importInfo(getInfo());
    }

    /**
     * 组装Info对象
     * @return
     */
    public static Info getInfo() {
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
