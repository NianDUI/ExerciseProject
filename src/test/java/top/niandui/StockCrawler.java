package top.niandui;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 应用启动自动加载
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/11/6 23:22
 */
public class StockCrawler {
    private static final String PATH = "I:\\Java\\6_project\\StockSpider\\outPutFile\\";
    private final WebClient webClient;

    /*** 初始化浏览器*/
    private StockCrawler() {
        // 新建一个模拟chrome的浏览器客户端对象
        webClient = new WebClient(BrowserVersion.CHROME);
        // 相应选项设置
        // 当JS执行出错的时候是否抛出异常
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        // 当HTTP的状态非200时是否抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setActiveXNative(false);
        // 是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        // 很重要，设置支持 Ajax
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setTimeout(30000);
    }

    private String crawler(String url) {
        try {
            WebRequest request = new WebRequest(new URL(url), HttpMethod.GET);
            Map<String, String> additionalHeaders = new HashMap<>();
            additionalHeaders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36");
            additionalHeaders.put("Accept-Language", "zh-CN,zh;q=0.8");
            additionalHeaders.put("Accept", "*/*");
            // 设置请求头信息
            request.setAdditionalHeaders(additionalHeaders);
            HtmlPage page = webClient.getPage(url);
            // 将响应信息转为String并返回

//            return page.getWebResponse().getContentAsString();
            return page.asNormalizedText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void work(String url) {
        try {
            // 打开网页
            webClient.getPage(url);
            int pageCount = 5;
            for (int i = 1; i <= pageCount; i++) {
                Thread.sleep(500);
                // 分页进行抓取
                String pageUrl = "http://q.10jqka.com.cn/index/index/board/all/field/zdf/order/desc/page/" + i + "/ajax/1/";
                String response = crawler(pageUrl);
                if (!response.contains("加自选")) {
                    return;
                } else {
                    saveFile(response, PATH + i + ".html", i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*** 保存抓取到的信息** @param content* @param path*/
    private static void saveFile(String content, String path, int i) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            file.delete();
        }
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            System.out.println("第" + i + "页保存成功！");
        } catch (Exception e) {
            System.out.println("第" + i + "页保存失败！");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        StockCrawler crawler = new StockCrawler();
        String crawler1 = crawler.crawler("https://www.tsxsw.net/html/131/131154/1.html");
        System.out.println("crawler1 = " + crawler1);
        String indexUrl = "http://q.10jqka.com.cn/";
//        crawler.work(indexUrl);
    }

}
