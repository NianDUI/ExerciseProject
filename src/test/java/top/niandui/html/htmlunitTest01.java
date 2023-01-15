package top.niandui.html;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import top.niandui.utils.WebClientUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 代理测试
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/25 9:24
 */
@Slf4j
public class htmlunitTest01 {

    static {
        // 设置日志显示等级
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = context.getLogger("root");
        root.setLevel(Level.ERROR);
    }

    @Test
    public void getIp() throws Exception {

        WebClient client = WebClientUtil.getWebClient();
        WebClientOptions webClientOptions = client.getOptions();

        ProxyConfig proxyConfig = webClientOptions.getProxyConfig();
//        proxyConfig.setSocksProxy(true);
//        proxyConfig.setProxyHost("121.205.253.164");
//        proxyConfig.setProxyPort(8118);
//        webClientOptions.setProxyConfig(proxyConfig);

//        HtmlPage page = client.getPage("https://www.ip138.com/");

        String url = "https://www.147xs.org/book/150820/226230160.html";
        WebRequest request = new WebRequest(UrlUtils.toUrlUnsafe(url), "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
                "gzip, deflate, br");
        request.setCharset(UTF_8);
        request.setAdditionalHeader(":authority", "www.147xs.org");
        request.setAdditionalHeader(":method", "GET");
        request.setAdditionalHeader(":path", "/book/150820/226230160.html");
        request.setAdditionalHeader(":scheme", "https");
//        request.setAdditionalHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//        request.setAdditionalHeader("accept-encoding", "gzip, deflate, br");
        request.setAdditionalHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8");
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        System.out.println(now);
        String since = String.format("%s, %03d %s %s %s GMT", now.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                , now.getDayOfMonth(), now.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                , now.getYear(), now.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println("since = " + since);
        request.setAdditionalHeader("if-modified-since", "Wed, 11 Jan 2023 03:52:29 GMT");
        request.setAdditionalHeader("sec-ch-ua", "\"Not?A_Brand\";v=\"8\", \"Chromium\";v=\"108\", \"Google Chrome\";v=\"108\"");
        request.setAdditionalHeader("sec-ch-ua-mobile", "?0");
        request.setAdditionalHeader("sec-ch-ua-platform", "Windows");
        request.setAdditionalHeader("sec-fetch-dest", "document");
        request.setAdditionalHeader("sec-fetch-mode", "navigate");
        request.setAdditionalHeader("sec-fetch-site", "same-origin");
        request.setAdditionalHeader("sec-fetch-user", "?1");
        request.setAdditionalHeader("upgrade-insecure-requests", "1");
        request.setAdditionalHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");

//        client.addRequestHeader();

        HtmlPage page = client.getPage(url);
        System.out.println(" a" + LocalDateTime.now());
        // 2019/1/4 10秒钟是为了让js能够充分执行（特别是ajax）
        client.waitForBackgroundJavaScript(10*1000);
        client.setJavaScriptTimeout(5*1000);
        System.out.println(" b" + LocalDateTime.now());

        page = (HtmlPage) page.refresh();

        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            String text = page.asNormalizedText();
            System.out.println("\ntext = \n" + text);
        }
        String s1 = page.asXml();
        System.out.println("s1 = " + s1);
//        Stream.of(text.split("\r?\n")).filter(s -> s.contains("本机IP: "))
//                .forEach(System.out::println);
    }
}
