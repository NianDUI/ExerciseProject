package top.niandui.ivi;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.slf4j.LoggerFactory;
import top.niandui.utils.WebClientUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * IVI测试频道列表获取
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/1/20 15:03
 */
public class IVITest01 {
    static {
        // 设置日志显示等级
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = context.getLogger("root");
        root.setLevel(Level.ERROR);
    }

    public static void main(String[] args) throws Exception {

        WebClient webClient = WebClientUtil.getWebClient();
        HtmlPage page = webClient.getPage("http://ivi.bupt.edu.cn");
        System.out.println(page.asXml());

        // //div[@class='content']/h1/text()
        List<HtmlDivision> divList = page.getByXPath("//div[@class='2u -2u' or @class='2u']");
        Map<String, Map<String, String>> mapMap = divList.stream().collect(Collectors.toMap(
                // CCTV-1高清
                div -> ((HtmlParagraph) div.getFirstByXPath("./p")).asNormalizedText()
                , div -> {
                    List<HtmlAnchor> aList = div.getByXPath("./a");
                    return aList.stream().collect(Collectors.toMap(
                            // PC端
                            DomNode::asNormalizedText,
                            // http://ivi.bupt.edu.cn/player.html?channel=cctv1hd
                            a -> a.getBaseURI() + a.getHrefAttribute()
                            , (s, s2) -> s
                            , LinkedHashMap::new
                    ));
                }
                , (map, map2) -> map
                , LinkedHashMap::new
        ));

        System.out.println(new ObjectMapper().writeValueAsString(mapMap));
        System.out.println();
    }
}
