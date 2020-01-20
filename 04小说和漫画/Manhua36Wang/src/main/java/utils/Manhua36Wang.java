package top.niandui.utils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Manhua36Wang {
    //新建一个模拟谷歌Chrome浏览器的浏览器客户端对象
    private final static WebClient webClient = new WebClient(BrowserVersion.CHROME);

    static {
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


    public static List<String> getChapterImgSrcs(String url) {
        List<String> imgSrcs = new ArrayList<String>();
        try {
            HtmlPage htmlPage = webClient.getPage(url);
            while (true) {
                String indexInfo = null;
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
                if (indexInfo != null && indexInfo.subSequence(indexInfo.indexOf('(') + 1, indexInfo.indexOf('/'))
                        .equals(indexInfo.substring(indexInfo.indexOf('/') + 1, indexInfo.indexOf(')')))) {
                    break;
                }
                HtmlAnchor next = (HtmlAnchor) htmlPage.getByXPath("//span[@class='nextPage']/..").get(0);
                htmlPage = next.click();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return imgSrcs;
    }
}
