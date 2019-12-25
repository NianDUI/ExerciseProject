package top.niandui.img_download.utils;

import com.gargoylesoftware.css.parser.CSSErrorHandler;
import com.gargoylesoftware.css.parser.CSSException;
import com.gargoylesoftware.css.parser.CSSParseException;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.NoOpLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * WebClient
 */
public class WebClientUtils {
    private WebClientUtils() {
    }

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
        webClientOptions.setJavaScriptEnabled(true);
        //很重要，设置支持AJAX
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());

//        webClient.setIncorrectnessListener((message, origin) -> {});
//        webClient.setCssErrorHandler(new CSSErrorHandler() {
//            @Override
//            public void warning(CSSParseException e) throws CSSException {
//            }
//            @Override
//            public void error(CSSParseException e) throws CSSException {
//            }
//            @Override
//            public void fatalError(CSSParseException e) throws CSSException {
//            }
//        });
//        webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {
//            @Override
//            public void scriptException(HtmlPage page, ScriptException scriptException) {
//            }
//            @Override
//            public void timeoutError(HtmlPage page, long allowedTime, long executionTime) {
//            }
//            @Override
//            public void malformedScriptURL(HtmlPage page, String url, MalformedURLException malformedURLException) {
//            }
//            @Override
//            public void loadScriptError(HtmlPage page, URL scriptUrl, Exception exception) {
//            }
//            @Override
//            public void warn(String message, String sourceName, int line, String lineSource, int lineOffset) {
//            }
//        });
//        webClient.setHTMLParserListener(new HTMLParserListener() {
//            @Override
//            public void error(String message, URL url, String html, int line, int column, String key) {
//            }
//            @Override
//            public void warning(String message, URL url, String html, int line, int column, String key) {
//            }
//        });
    }

    public static WebClient getWebClient() {
        return webClient;
    }


}
