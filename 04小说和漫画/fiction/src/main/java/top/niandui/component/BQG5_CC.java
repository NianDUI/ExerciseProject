package top.niandui.component;

import top.niandui.model.IBaseComponent;
import top.niandui.model.Info;
import top.niandui.utils.PrintUtil;

/**
 * @Title: BQG5_CC.java
 * @description: BQG5_CC
 * @time: 2020/1/29 12:39
 * @author: liyongda
 * @version: 1.0
 */
public class BQG5_CC implements IBaseComponent {

    public static void main(String[] args) {
        new BQG5_CC().start();
    }

    @Override
    public void startBeforePrint() {
        /*
         * 信息空间.txt https://www.bqg5.cc/128_128660/171885147.html
         */
        PrintUtil.println("\thttp://www.bqg5.cc");
    }

    @Override
    public Info getInfo() {
        Info info = new Info();
        info.titleXPathExpr = "//div[@class='bookname']/h1/text()";
        info.customizeTitleHandler();
        info.contentXPathExpr = "//div[@id='content']/p/text()";
        info.anchorXPathExpr = "//div[@class='bottem2']//a";
        info.nextAnchorIndex = 3;
        return info;
    }
}
