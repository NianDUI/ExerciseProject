package top.niandui.utils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import top.niandui.model.Info;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
        //开启https
        webClientOptions.setUseInsecureSSL(true);
        //很重要，设置支持AJAX
        WEB_CLIENT.setAjaxController(new NicelyResynchronizingAjaxController());
    }

    // 测试数据校验
    @Test
    public void test() {
        Info info = new Info();
        info.startUrl = "http://localhost:9001/doc.html/阿斯顿发?ie=UTF-8&wd=deque等&xx=xx";
//        info.startUrl = "http://localhost:9001/doc.html/阿斯顿发";
        info.contentStartIndexOffset = -1;
        getContext(info);
    }

    /**
     * 输入相关信息
     *
     * @param info 获取内容，必要的信息
     */
    public static void importInfo(final Info info) {
        try {
            Scanner sc = new Scanner(System.in);
            if (StringUtils.isBlank(info.startUrl)) {
                String line = StringUtils.EMPTY;
                while ("".equals(line)) {
                    PrintUtil.print("请输入章节地址：");
                    line = sc.nextLine().trim();
                }
                info.startUrl = line;
            }
            if (StringUtils.isBlank(info.fileName)) {
                String line = StringUtils.EMPTY;
                while ("".equals(line)) {
                    PrintUtil.print("请输入保存文件名称：");
                    line = sc.nextLine().trim();
                }
                info.fileName = line;
            }
            if (!info.fileName.endsWith(".txt")) {
                info.fileName += ".txt";
            }
            PrintUtil.println("写对象初始化");
            info.fileWriter.init();
            PrintUtil.println("获取文本...");
            getContext(info);
            PrintUtil.println("获取完毕");
            if (info.isSaveFile) {
                info.fileWriter.endWrite();
                PrintUtil.println("结束写");
            }
        } catch (Exception e) {
            info.fileWriter.endWrite();
            PrintUtil.waitPrintEnd(10);
            e.printStackTrace();
        }
        SystemUtil.exit(0);
    }

    /**
     * 获取小说各章节内容
     *
     * @param info 获取内容，必要的信息
     * @return
     */
    public static void getContext(final Info info) {
        // 数据校验
        Set<ConstraintViolation<Info>> violationSet = VALIDATOR.validate(info);
        for (ConstraintViolation<Info> violation : violationSet) {
            PrintUtil.println("------------------------------");
            PrintUtil.println(violation.getConstraintDescriptor().getAttributes());
            String message = violation.getRootBeanClass() + ": " + violation.getPropertyPath() + " = " + violation.getInvalidValue() + "; " + violation.getMessage();
            PrintUtil.println(message);
        }
        if (violationSet.size() > 0) {
            SystemUtil.exit(0);
        }
        try {
            // 获取开始结束时间
            long startTime = System.currentTimeMillis(), endTimes;
            // 获取起始页面
            HtmlPage htmlPage = WEB_CLIENT.getPage(info.startUrl);
            while (true) {
                // 获取标题DOM列表
                List titleList = htmlPage.getByXPath(info.titleXPathExpr);
                // 调用自定义方法处理标题
                String title;
                try {
                    title = info.titleHandler.apply(titleList.get(0).toString().trim());
                } catch (Exception e) {
                    // 获取内容出错时，为服务端限制，重新拉去该页面。
                    // Index: 0, Size: 0
                    PrintUtil.println(e.getMessage());
                    // 调用休眠处理方法
                    info.sleepHandler.get();
                    startTime = System.currentTimeMillis();
                    htmlPage = WEB_CLIENT.getPage(htmlPage.getUrl());
                    continue;
                }
                // 计算使用时间
                endTimes = System.currentTimeMillis();
                PrintUtil.println(title + " " + (endTimes - startTime) / 1000.0 + "s");
                startTime = endTimes;
                StringBuilder sb = new StringBuilder().append(title).append(info.titleNewLine);
                // 获取内容DOM列表
                List list = htmlPage.getByXPath(info.contentXPathExpr);
                for (int i = info.contentStartIndexOffset; i < list.size() + info.contentEndIndexOffset; i++) {
                    sb.append(list.get(i).toString()).append(info.contentNewLine);
                }
                info.fileWriter.write(sb);
                // 获取跳转超链接DOM列表
                List aList = htmlPage.getByXPath(info.anchorXPathExpr);
                // 获取下一页的超链接DOM
                HtmlAnchor next = (HtmlAnchor) aList.get(info.nextAnchorIndex);
                // 页面链接
                String[] pageLink = {
                        // 本页链接
                        htmlPage.getUrl().toString().trim(),
                        // 下一页链接
                        next.getHrefAttribute().trim()
                };
                // 调用自定义方法判断下一页是否还有内容
                if (info.isEndHref.apply(pageLink, sb)) {
                    break;
                }
                // 跳转下一页
                htmlPage = next.click();
            }
        } catch (Exception e) {
            PrintUtil.println("获取失败...");
            throw new RuntimeException(e);
        }
    }

    /**
     * 保存内容到文件
     *
     * @param fileName 文件路径+名称
     * @param list     要写入到文件的内容
     * @param append   是否追加写入
     */
    @SneakyThrows
    public static void saveFile(String fileName, List<String> list, boolean append) {
        File target = new File("target");
        if (!target.exists()) {
            target.mkdirs();
        }
        String pathname = "target/" + fileName;
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathname, append), StandardCharsets.UTF_8));
        for (String str : list) {
            bw.write(str);
        }
        bw.flush();
        bw.close();
    }
}
