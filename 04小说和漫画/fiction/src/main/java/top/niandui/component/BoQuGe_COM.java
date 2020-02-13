package top.niandui.component;

import top.niandui.model.IBaseComponent;
import top.niandui.model.Info;
import top.niandui.utils.PrintUtil;

/**
 * @Title: BoQuGe_COM.java
 * @description: BoQuGe_COM
 * @time: 2020/1/19 11:22
 * @author: liyongda
 * @version: 1.0
 */
public class BoQuGe_COM implements IBaseComponent {

    public static void main(String[] args) {
        new BoQuGe_COM().start();
    }

    @Override
    public void startBeforePrint() {
        /*
         * 大明的工业革命.txt https://www.boquge.com/book/116114/167888770.html
         * https://www.boquge.com/book/116114/169505845.html
         */
        PrintUtil.println("\thttp://www.boquge.com");
    }

    @Override
    public Info getInfo() {
        Info info = new Info();
        info.titleXPathExpr = "//div[@id='h1']/h1/text()";
        info.customizeTitleHandler();
        info.contentXPathExpr = "//div[@id='txtContent']/text()";
        info.anchorXPathExpr = "//div[@class='clearfix']/ul/li/a";
        info.nextAnchorIndex = 6;
        return info;
    }
}
