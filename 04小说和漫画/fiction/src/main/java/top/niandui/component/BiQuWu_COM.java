package top.niandui.component;

import top.niandui.model.IBaseComponent;
import top.niandui.model.Info;
import top.niandui.utils.PrintUtil;

/**
 * @Title: BiQuWu_COM.java
 * @description: BiQuWu_COM
 * @time: 2020/1/20 14:46
 * @author: liyongda
 * @version: 1.0
 */
public class BiQuWu_COM implements IBaseComponent {

    public static void main(String[] args) {
        new BiQuWu_COM().start();
    }

    @Override
    public void startBeforePrint() {
        /*
         * 重写科技格局.txt https://www.biqiwu.com/b/219/219203/63725271.html
         */
        PrintUtil.println("\thttp://www.biqiwu.com");
    }

    @Override
    public Info getInfo() {
        Info info = new Info();
        info.titleXPathExpr = "//div[@class='bookname']/h1/text()";
        info.customizeTitleHandler();
        info.contentXPathExpr = "//div[@id='content']/text()";
        info.contentNewLine = "\r\n";
        info.anchorXPathExpr = "//div[@class='bottem2']//a";
        info.nextAnchorIndex = 3;
        info.isAppendWrite = true;
        return info;
    }
}
