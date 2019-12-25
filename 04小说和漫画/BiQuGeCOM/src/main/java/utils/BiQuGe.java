package utils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.List;

public class BiQuGe {
    //新建一个模拟谷歌Chrome浏览器的浏览器客户端对象
    private final static WebClient webClient = new WebClient(BrowserVersion.CHROME);

    static {
        WebClientOptions webClientOptions = webClient.getOptions();
        //当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClientOptions.setThrowExceptionOnScriptError(false);
        //当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClientOptions.setThrowExceptionOnFailingStatusCode(false);
        webClientOptions.setActiveXNative(false);
        //是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClientOptions.setCssEnabled(false);
        //很重要，启用JS
        webClientOptions.setJavaScriptEnabled(false);
        //很重要，设置支持AJAX
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
    }


    public static List<String> getContext(String url) {
        List<String> stringList = new ArrayList<String>();
        try {
            HtmlPage htmlPage = webClient.getPage(url);
            while (true) {
                List title = htmlPage.getByXPath("//div[@class='bookname']/h1/text()");
                System.out.println(title.get(0).toString());
                StringBuilder sb = new StringBuilder().append(title.get(0).toString()).append("\r\n\r\n");
                List list = htmlPage.getByXPath("//div[@id='content']/text()");
                for (int i = 0; i < list.size() - 3; i++) {
                    sb.append(list.get(i).toString()).append("\r\n");
                }
                stringList.add(sb.toString());
                List aList = htmlPage.getByXPath("//div[@class='bottem2']//a");

                HtmlAnchor next = (HtmlAnchor) aList.get(3);
                if (!next.getHrefAttribute().contains("html") && !next.getHrefAttribute().contains("HTML")) {
                    break;
                }
                htmlPage = next.click();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringList;
    }
}
