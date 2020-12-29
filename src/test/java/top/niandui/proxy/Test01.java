package top.niandui.proxy;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import top.niandui.utils.WebClientUtil;

import java.util.stream.Stream;

/**
 * 代理测试
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/25 9:24
 */
@Slf4j
public class Test01 {

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
        proxyConfig.setSocksProxy(true);
        proxyConfig.setProxyHost("127.0.0.1");
        proxyConfig.setProxyPort(10080);
//        webClientOptions.setProxyConfig(proxyConfig);

//        HtmlPage page = client.getPage("https://www.ip138.com/");
        HtmlPage page = client.getPage("https://www.baidu.com/s?ie=UTF-8&wd=ip");
        String text = page.asText();
//        System.out.println("text = " + text);
        Stream.of(text.split("\r\n")).filter(s -> s.contains("本机IP: "))
                .forEach(System.out::println);
    }
}
