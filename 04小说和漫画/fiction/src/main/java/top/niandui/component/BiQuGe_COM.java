package top.niandui.component;

import top.niandui.model.IBaseComponent;
import top.niandui.model.Info;
import top.niandui.utils.PrintUtil;

/**
 * @Title: BiQuGe_COM.java
 * @description: BiQuGe_COM
 * @time: 2020/1/19 11:00
 * @author: liyongda
 * @version: 1.0
 */
public class BiQuGe_COM implements IBaseComponent {

    public static void main(String[] args) {
        new BiQuGe_COM().start();
    }

    @Override
    public void startBeforePrint() {
        /*
         * 联盟一姐的生活手册.txt http://www.biqige.com/107_107926/35015270.html
         */
        PrintUtil.println("\thttp://www.biqige.com");
    }

    @Override
    public Info getInfo() {
        Info info = new Info();
        info.titleXPathExpr = "//div[@class='bookname']/h1/text()";
        info.customizeTitleHandler();
        info.contentXPathExpr = "//div[@id='content']/text()";
        info.contentEndIndexOffset = -3;
        info.anchorXPathExpr = "//div[@class='bottem2']//a";
        info.nextAnchorIndex = 3;
        return info;
    }
}
