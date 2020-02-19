package top.niandui.component;

import top.niandui.model.IBaseComponent;
import top.niandui.model.Info;
import top.niandui.utils.PrintUtil;

import java.lang.reflect.Method;

/**
 * @Title: BiQuGe_INFO.java
 * @description: BiQuGe_INFO
 * @time: 2020/1/19 11:22
 * @author: liyongda
 * @version: 1.0
 */
public class BiQuGe_INFO implements IBaseComponent {

    public static void main(String[] args) {
        new BiQuGe_INFO().start();
    }

    @Override
    public void startBeforePrint() {
        /*
         * 饲养全人类.txt http://www.biquge.info/69_69120/12898729.html
         */
        PrintUtil.println("\thttp://www.biquge.info");
    }

    @Override
    public Info getInfo() {
        Info info = new Info();
        info.titleXPathExpr = "//div[@class='bookname']/h1/text()";
        info.customizeTitleHandler();
        info.contentXPathExpr = "//div[@id='content']/text()";
        info.anchorXPathExpr = "//div[@class='bottem']//a";
        info.nextAnchorIndex = 3;
        info.customizeWriteType();
        return info;
    }
}
