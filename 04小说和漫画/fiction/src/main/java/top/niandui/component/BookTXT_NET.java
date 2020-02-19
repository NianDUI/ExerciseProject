package top.niandui.component;

import top.niandui.model.IBaseComponent;
import top.niandui.model.Info;
import top.niandui.utils.PrintUtil;

import javax.swing.border.TitledBorder;

/**
 * @Title: BookTXT_NET.java
 * @description: BookTXT_NET
 * @time: 2020/1/20 14:46
 * @author: liyongda
 * @version: 1.0
 */
public class BookTXT_NET implements IBaseComponent {

    public static void main(String[] args) {
        new BookTXT_NET().start();
    }

    @Override
    public void startBeforePrint() {
        /*
         * 巫师不朽.txt https://www.booktxt.net/6_6254/2363524.html
         */
        PrintUtil.println("\thttp://www.booktxt.net");
        PrintUtil.println("\thttp://www.bxquge.com");
        // bxquge.com: info.titleHandler = title -> title.substring(6);
    }

    @Override
    public Info getInfo() {
        Info info = new Info();
        info.titleXPathExpr = "//div[@class='bookname']/h1/text()";
        info.customizeTitleHandler();
        info.contentXPathExpr = "//div[@id='content']/text()";
        info.anchorXPathExpr = "//div[@class='bottem2']//a";
        info.nextAnchorIndex = 3;
        info.customizeWriteType();
        return info;
    }
}
