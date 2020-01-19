package utils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.metadata.ConstraintDescriptor;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Title: WebClientUtil.java
 * @description: WebClientUtil
 * @time: 2020/1/17 11:00
 * @author: liyongda
 * @version: 1.0
 */
public class WebClientUtil {
    //新建一个模拟谷歌Chrome浏览器的浏览器客户端对象
    private final static WebClient WEB_CLIENT = new WebClient(BrowserVersion.CHROME);
    // 数据校验器
    private final static Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    static {
        WebClientOptions webClientOptions = WEB_CLIENT.getOptions();
        //当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClientOptions.setThrowExceptionOnScriptError(false);
        //当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClientOptions.setThrowExceptionOnFailingStatusCode(false);
        webClientOptions.setActiveXNative(false);
        //是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClientOptions.setCssEnabled(false);
        //是否启用JS
        webClientOptions.setJavaScriptEnabled(false);
        //很重要，设置支持AJAX
        WEB_CLIENT.setAjaxController(new NicelyResynchronizingAjaxController());
    }

    @Test
    public void test() {
        Info info = new Info();
        getContext(info);
    }

    public static List<String> getContext(Info info) {
        Set<ConstraintViolation<Info>> violationSet = VALIDATOR.validate(info);
        for (ConstraintViolation<Info> violation : violationSet) {
            System.out.println(violation.getConstraintDescriptor());
            String message = violation.getRootBeanClass() + ": " + violation.getPropertyPath() + " = " + violation.getInvalidValue() + "; " + violation.getMessage();
            System.out.println(message);
            System.out.println("------------------------------");
        }
        if (violationSet.size() > 0) {
            System.exit(0);
        }
        List<String> stringList = new ArrayList<>();
        try {
            HtmlPage htmlPage = WEB_CLIENT.getPage(info.startUrl);
            while (true) {
                List title = htmlPage.getByXPath(info.titleXPathExpr);
                System.out.println(title.get(0).toString());
                StringBuilder sb = new StringBuilder().append(title.get(0).toString()).append(info.titleNewLine);
                List list = htmlPage.getByXPath(info.contentXPathExpr);
                for (int i = 0 + info.contentStartIndexOffset; i < list.size() + info.contentEndIndexOffset; i++) {
                    sb.append(list.get(i).toString()).append(info.contentNewLine);
                }
                stringList.add(sb.toString());
                List aList = htmlPage.getByXPath(info.anchorXPathExpr);
                HtmlAnchor next = (HtmlAnchor) aList.get(info.nextAnchorIndex);
                if (info.isEndHref.apply(next.getHrefAttribute())) {
                    break;
                }
                htmlPage = next.click();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringList;
    }

    public static void saveFile(String fileName, List<String> list) {
        File target = new File("target");
        if (!target.exists()) {
            target.mkdirs();
        }
        String pathname = "target/" + fileName;
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathname), StandardCharsets.UTF_8));
            for (String str : list) {
                bw.write(str);
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
