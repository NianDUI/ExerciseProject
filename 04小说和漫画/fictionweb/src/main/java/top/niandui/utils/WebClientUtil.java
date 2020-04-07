package top.niandui.utils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import top.niandui.dao.IChapterDao;
import top.niandui.dao.IParagraphDao;
import top.niandui.model.Book;
import top.niandui.model.Chapter;
import top.niandui.model.Config;
import top.niandui.model.Paragraph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @Title: WebClientUtil.java
 * @description: WebClientUtil
 * @time: 2020/1/17 11:00
 * @author: liyongda
 * @version: 1.0
 */
@Slf4j
public class WebClientUtil {
    //新建一个模拟谷歌Chrome浏览器的浏览器客户端对象
    private final static WebClient WEB_CLIENT = new WebClient(BrowserVersion.CHROME);

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

    /**
     * 获取书籍各章节内容
     *
     * @param config        配置
     * @param book          书籍
     * @param seqid         章节序号
     * @param isFirstJump   第一次是否跳过,t跳过、f不跳过
     * @param iChapterDao   章节dao
     * @param iParagraphDao 段落dao
     */
    public static void getBook(Config config, Book book, long seqid, boolean isFirstJump, IChapterDao iChapterDao, IParagraphDao iParagraphDao) {
        Function<String, String> titleHandler = HandleUtils.getTitleHandler(book.getTitlehandler());
        try {
            // 获取开始结束时间
            long startTime = System.currentTimeMillis(), endTimes;
            // 获取起始页面
            HtmlPage htmlPage = WEB_CLIENT.getPage(book.getStarturl());
            while (true) {
                String url = htmlPage.getUrl().toString().trim();
                if (!isFirstJump) {
                    Chapter chapter = new Chapter();
                    // 获取标题DOM列表
                    List titleList = htmlPage.getByXPath(config.getTitlematch());
                    // 调用自定义方法处理标题
                    try {
                        chapter.setRawname(titleList.get(0).toString().trim());
                        String title = titleHandler.apply(chapter.getRawname());
                        chapter.setName(title);
                        log.info(chapter.getRawname() + " -> " + title);
                    } catch (Exception e) {
                        // 获取内容出错时，为服务端限制，重新拉去该页面。
                        // Index: 0, Size: 0
                        log.info(e.getMessage());
                        // 调用休眠处理方法
                        HandleUtils.sleepHandler.get();
                        startTime = System.currentTimeMillis();
                        htmlPage = WEB_CLIENT.getPage(htmlPage.getUrl());
                        continue;
                    }
                    // 计算使用时间
                    endTimes = System.currentTimeMillis();
                    log.info(chapter.getName() + " " + (endTimes - startTime) / 1000.0 + "s");
                    startTime = endTimes;
                    chapter.setBookid(book.getBookid());
                    chapter.setConfigid(book.getConfigid());
                    // 本页链接
                    chapter.setUrl(url);
                    chapter.setSeqid(seqid++);
                    // 创建章节
                    iChapterDao.create(chapter);
                    // 获取内容DOM列表
                    List list = htmlPage.getByXPath(config.getConmatch());
                    List<Paragraph> paragraphList = new ArrayList<>();
                    for (int i = config.getStartoffset(); i < list.size() + config.getEndoffset(); i++) {
                        String line = list.get(i).toString().trim();
                        int start = 0;
                        while (line.startsWith("　", start)) {
                            start++;
                        }
                        line = line.substring(start).trim();
                        if (line.length() == 0) {
                            continue;
                        }
                        Paragraph paragraph = new Paragraph();
                        paragraph.setBookid(book.getBookid());
                        paragraph.setChapterid(chapter.getChapterid());
                        paragraph.setContent(line);
                        paragraph.setSeqid((long) i);
                        paragraphList.add(paragraph);
                    }
                    // 创建段落列表
                    iParagraphDao.createBatch(paragraphList);
                }
                if (isFirstJump) {
                    isFirstJump = false;
                }
                // 获取跳转超链接DOM列表
                List aList = htmlPage.getByXPath(config.getAmatch());
                // 获取下一页的超链接DOM
                HtmlAnchor next = (HtmlAnchor) aList.get(config.getNexta());
                // 调用自定义方法判断下一页是否还有内容
                if (HandleUtils.isEndHref.apply(url, next.getHrefAttribute().trim())) {
                    break;
                }
                // 跳转下一页
                htmlPage = next.click();
            }
            log.info("获取结束...");
        } catch (Exception e) {
            log.info("获取失败...");
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }
}
