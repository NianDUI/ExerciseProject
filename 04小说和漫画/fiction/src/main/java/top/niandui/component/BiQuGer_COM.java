package top.niandui.component;

import top.niandui.model.IBaseComponent;
import top.niandui.model.Info;
import top.niandui.utils.PrintUtil;

/**
 * @Title: BiQuGer_COM.java
 * @description: BiQuGer_COM
 * @time: 2020/1/19 11:22
 * @author: liyongda
 * @version: 1.0
 */
public class BiQuGer_COM implements IBaseComponent {

    public static void main(String[] args) {
        new BiQuGer_COM().start();
    }

    @Override
    public void startBeforePrint() {
        /*
         * 魔法的学术时代.txt http://www.biquger.com/biquge/112679/45424996
         */
        PrintUtil.println("\thttp://www.biquger.com");
    }

    @Override
    public Info getInfo() {
        Info info = new Info();
        info.titleXPathExpr = "//div[@class='bookname']/h1/text()";
        info.customizeTitleHandler();
        info.contentXPathExpr = "//div[@class='content']/text()";
        info.contentNewLine = "\r\n";
        info.anchorXPathExpr = "//div[@class='bottem']//a";
        info.nextAnchorIndex = 3;
        info.isEndHref = (nextPageUrl, content) -> nextPageUrl.endsWith("/");
        return info;
    }
}
