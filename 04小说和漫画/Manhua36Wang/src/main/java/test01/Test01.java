package test01;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.*;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test01 {

    //新建一个模拟谷歌Chrome浏览器的浏览器客户端对象
    final WebClient webClient = new WebClient(BrowserVersion.CHROME);

    public Test01() {
        WebClientOptions webClientOptions =  webClient.getOptions();
        //当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClientOptions.setThrowExceptionOnScriptError(false);
        //当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClientOptions.setThrowExceptionOnFailingStatusCode(false);
        webClientOptions.setActiveXNative(false);
        //是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClientOptions.setCssEnabled(false);
        //很重要，启用JS
        webClientOptions.setJavaScriptEnabled(true);
        //很重要，设置支持AJAX
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
    }

    public static void main(String[] args) {
        new Test01().test02();
    }

    @Test
    public void test01() {
        try {
            HtmlPage htmlPage;
            htmlPage = webClient.getPage("https://www.36mh.com/manhua/daijiachengxiang/223690.html");
            DomNode imageDiv = htmlPage.querySelector("#images");
            System.out.println(imageDiv.asXml());
            DomNode img = imageDiv;
            boolean bl = true;
            for (int i = 0; bl && i < 50; i++) {
                Thread.sleep(20);
                img = imageDiv.querySelector("img");
                if (img != null) {
                    System.out.println(img.asXml());
                    bl = false;
                }
                System.out.println(i);
            }
            System.out.println(img.getByXPath("@src"));


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test02() {
        try {
            HtmlPage htmlPage;
            htmlPage = webClient.getPage("https://www.36mh.com/manhua/daijiachengxiang/223690.html");
            String src = null, indexInfo = null;
            boolean bl = true;
            for (int i = 0; bl && i < 50; i++) {
                Thread.sleep(20);
                List list = htmlPage.getByXPath("//div[@id='images']/img/@src | //div[@id='images']/p/text()");
                System.out.println(list);
                if (list != null && list.size() > 1) {
                    src = ((DomAttr)list.get(0)).getValue();
                    indexInfo = ((DomText)list.get(1)).getWholeText();
                    bl = false;
//                    System.out.println(htmlPage.getByXPath("//span[@class='nextPage']/..")); // HtmlAnchor
                }
                System.out.println(i);
            }
            if (!bl) {
                System.out.println(src);
                System.out.println(indexInfo);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test03() {
        List<String> imgSrcs = new ArrayList<String>();
        try {
                HtmlPage htmlPage = webClient.getPage("https://www.36mh.com/manhua/daijiachengxiang/223690.html");
            String indexInfo = null;
            while (true) {
                if (indexInfo != null && indexInfo.subSequence(indexInfo.indexOf('(') + 1, indexInfo.indexOf('/'))
                        .equals(indexInfo.substring(indexInfo.indexOf('/') + 1, indexInfo.indexOf(')')))) {
                    System.out.println(htmlPage.getUrl());
                    break;
                }
                boolean bl = true;
                for (int i = 0; bl && i < 50; i++) {
                    String xpathExpr = "//div[@id='images']/img/@src | //div[@id='images']/p/text()";
                    List list = htmlPage.getByXPath(xpathExpr);
                    System.out.println(list);
                    if (list != null && list.size() > 1) {
                        imgSrcs.add(((DomAttr)list.get(0)).getValue());
                        indexInfo = ((DomText)list.get(1)).getWholeText();
                        bl = false;
                    } else {
                        Thread.sleep(20);
                    }
                }
                HtmlAnchor next = (HtmlAnchor) htmlPage.getByXPath("//span[@class='nextPage']/..").get(0);
                htmlPage = next.click();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(imgSrcs);

    }

}
