package top.niandui.component;

import top.niandui.model.IBaseComponent;
import top.niandui.model.Info;
import top.niandui.utils.PrintUtil;

/**
 * @Title: MCMSSC_COM.java
 * @description: MCMSSC_COM
 * @time: 2020/1/19 11:22
 * @author: liyongda
 * @version: 1.0
 */
public class MCMSSC_COM implements IBaseComponent {

    public static void main(String[] args) {
        new MCMSSC_COM().start();
    }

    @Override
    public void startBeforePrint() {
        /*
         * 我的女友是声优.txt http://www.mcmssc.com/90_90956/38213658.html
         */
        PrintUtil.println("\thttp://www.mcmssc.com");
    }

    @Override
    public Info getInfo() {
        Info info = new Info();
        info.titleXPathExpr = "//div[@class='bookname']/h1/text()";
        info.titleNewLine = "";
        info.customizeTitleHandler();
        info.contentXPathExpr = "//div[@id='content']/text()";
        info.anchorXPathExpr = "//div[@class='bottem2']//a";
        info.nextAnchorIndex = 3;
        info.customizeWriteType();
        return info;
    }
}
