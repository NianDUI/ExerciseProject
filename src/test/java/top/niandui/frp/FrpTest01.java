package top.niandui.frp;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import top.niandui.utils.WebClientUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * frp日志请求ip过滤
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/5/18 9:06
 */
public class FrpTest01 {

    static {
        // 设置日志显示等级
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = context.getLogger("root");
        root.setLevel(Level.ERROR);
    }

    /**
     * 查询frp日志中连接 ip 和 相应地址
     */
    @Test
    public void test01() throws Exception {
        Set<String> ipSet = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream("D:/ztest/frps.log")))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!StringUtils.hasText(line)
                        // get a user connection [95.174.99.94:51223]
                        || !line.matches(".+get a user connection \\[\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+\\]")) {
                    // 跳过
                    continue;
                }
                String ip = line.substring(line.lastIndexOf('[') + 1, line.lastIndexOf(":")).trim();
                ipSet.add(ip);
            }

        }
        // 要排除的ip
        ipSet.remove("222.128.6.52");
        // 114.118.10.141=北京市北京市 电信&联通&移动 数据中心
        ipSet.remove("114.118.10.141");
        // 123.126.40.65=北京市北京市 联通
        ipSet.remove("123.126.40.65");
        // 打印输出
        ipSet.forEach(System.out::println);
        System.out.println("---------------");

        Map<String, String> ipMap = new HashMap<>(ipSet.size(), 1);
        for (String ip : ipSet) {
            Thread.sleep(50);
            // 获取ip位置信息
            ipMap.put(ip, getIpAddress(ip));
        }

        // 打印输出
        System.out.println("---------------");
        ipMap.entrySet().forEach(System.out::println);
        System.out.println("---------------");
        ipMap.keySet().forEach(ip -> System.out.println("sshd:" + ip + ":deny"));
    }

    // 获取ip位置信息
    public String getIpAddress(String ip) throws Exception {
        try {
            String url = "https://www.ip138.com/iplookup.asp?action=2&ip=" + ip;

            WebClient client = WebClientUtil.getWebClient();
            WebClientOptions options = client.getOptions();
            // 启用js
            options.setJavaScriptEnabled(true);

            HtmlPage page = client.getPage(url);
            List<DomText> byXPath = page.getByXPath("//div[@class='table-box']/table/tbody/tr/td/p/text()");

            // 组装结果返回
            StringJoiner sj = new StringJoiner(" ");
            byXPath.stream().map(DomText::toString).forEach(sj::add);
            return sj.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
