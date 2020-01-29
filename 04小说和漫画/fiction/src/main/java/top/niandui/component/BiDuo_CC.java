package top.niandui.component;

import top.niandui.model.IBaseComponent;
import top.niandui.model.Info;

/**
 * @Title: BiDuo_CC.java
 * @description: BiDuo_CC
 * @time: 2020/1/19 11:00
 * @author: liyongda
 * @version: 1.0
 */
public class BiDuo_CC implements IBaseComponent {

    public static void main(String[] args) {
        new BiDuo_CC().start();
    }

    @Override
    public void startBeforePrint() {
        /*
         * 腾飞我的航空时代.txt https://www.biduo.cc/biquge/53_53393/c22077441.html
         */
        System.out.println("http://www.biduo.cc");
    }

    @Override
    public Info getInfo() {
        Info info = new Info();
        info.titleXPathExpr = "//div[@class='bookname']/h1/text()";
        info.customizeTitleHandler();
        info.contentXPathExpr = "//div[@id='content']/text()";
        info.contentEndIndexOffset = -3;
        info.anchorXPathExpr = "//div[@class='bottem2']//a";
        info.nextAnchorIndex = 2;
        return info;
    }
}
