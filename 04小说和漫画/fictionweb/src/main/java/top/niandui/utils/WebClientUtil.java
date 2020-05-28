package top.niandui.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.niandui.common.expection.ReStateException;
import top.niandui.dao.IBookDao;
import top.niandui.dao.IChapterDao;
import top.niandui.dao.IParagraphDao;
import top.niandui.model.Book;
import top.niandui.model.Chapter;
import top.niandui.model.Config;
import top.niandui.model.Paragraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static top.niandui.utils.HandleUtils.getIsEndHref;

/**
 * @Title: WebClientUtil.java
 * @description: WebClientUtil
 * @time: 2020/1/17 11:00
 * @author: liyongda
 * @version: 1.0
 */
@Slf4j
@Component
public class WebClientUtil {
    // json处理对象
    public final static ObjectMapper json = new ObjectMapper();
    @Autowired
    private IBookDao iBookDao;
    @Autowired
    private IChapterDao iChapterDao;
    @Autowired
    private IParagraphDao iParagraphDao;

    //新建一个模拟谷歌Chrome浏览器的浏览器客户端对象
    public static WebClient getWebClient() {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        WebClientOptions webClientOptions = webClient.getOptions();
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
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        return webClient;
    }

    /**
     * 获取书籍各章节内容
     *
     * @param config      配置信息
     * @param book        书籍信息
     * @param seqid       章节序号
     * @param isFirstJump 第一次是否跳过,t跳过、f不跳过
     */
    @Async
    public void getBook(Config config, Book book, long seqid, boolean isFirstJump) {
        try {
            Map handleInfo = json.readValue(book.getHandlerinfo(), Map.class);
            Function<String, String> titleHandler = HandleUtils.getTitleHandler(handleInfo);
            BiFunction<String, String, Boolean> isEndHref = getIsEndHref(handleInfo);
            // 获取开始结束时间
            long startTime = System.currentTimeMillis(), endTimes;
            // 获取起始页面
            HtmlPage htmlPage = getWebClient().getPage(book.getStarturl());
            int errorNum = 0;
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
                        log.info(title + " <- " + chapter.getRawname());
                    } catch (Exception e) {
                        // 获取内容出错时，为服务端限制，重新拉去该页面。
                        // Index: 0, Size: 0
                        log.info(e.getMessage());
                        if (++errorNum > 10) {
                            throw new RuntimeException("当前章节一重复获取10次错误，已停止获取");
                        }
                        // 调用休眠处理方法
                        HandleUtils.sleepHandler.get();
                        startTime = System.currentTimeMillis();
                        htmlPage = getWebClient().getPage(htmlPage.getUrl());
                        continue;
                    }
                    errorNum = 0;
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
                    // 获取段落列表
                    List<Paragraph> paragraphList = getParagraphList(config, chapter, htmlPage);
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
                if (isEndHref.apply(url, next.getHrefAttribute().trim())) {
                    break;
                }
                // 跳转下一页
                htmlPage = next.click();
            }
            log.info("获取结束...");
        } catch (Exception e) {
            log.info("获取失败...");
            log.error("获取失败...");
            log.error(e.toString());
//            throw new RuntimeException(e);
        }
        // 更新任务状态
        iBookDao.updateTaskstatus(book.getBookid(), 0);
    }

    /**
     * 获取该章节段落内容
     *
     * @param config  配置信息
     * @param chapter 章节信息
     */
    public void getChapter(Config config, Chapter chapter) {
        try {
            // 获取开始结束时间
            long startTime = System.currentTimeMillis(), endTimes;
            // 获取起始页面
            HtmlPage htmlPage = getWebClient().getPage(chapter.getUrl());
            int num = 0;
            while (num++ < 10) {
                // 获取标题DOM列表
                List titleList = htmlPage.getByXPath(config.getTitlematch());
                // 标题是否存在
                String title;
                try {
                    title = titleList.get(0).toString().trim();
                } catch (Exception e) {
                    // 获取内容出错时，为服务端限制，重新拉去该页面。
                    // Index: 0, Size: 0
                    log.info(e.getMessage());
                    // 调用休眠处理方法
                    HandleUtils.sleepHandler.get();
                    startTime = System.currentTimeMillis();
                    htmlPage = getWebClient().getPage(htmlPage.getUrl());
                    continue;
                }
                // 计算使用时间
                endTimes = System.currentTimeMillis();
                log.info(title + " " + (endTimes - startTime) / 1000.0 + "s");
                // 删除原有段落信息
                iParagraphDao.deleteByChapterId(chapter.getChapterid().toString());
                // 获取段落列表
                List<Paragraph> paragraphList = getParagraphList(config, chapter, htmlPage);
                // 创建段落列表
                iParagraphDao.createBatch(paragraphList);
                break;
            }
            log.info("获取结束...");
        } catch (Exception e) {
            log.info("获取失败...");
            log.error(e.toString());
            // 更新任务状态
            iBookDao.updateTaskstatus(chapter.getBookid(), 0);
            throw new ReStateException("获取失败");
        }
        // 更新任务状态
        iBookDao.updateTaskstatus(chapter.getBookid(), 0);
    }

    /**
     * 获取段落列表
     *
     * @param config   配置信息
     * @param chapter  章节信息
     * @param htmlPage html页面信息
     * @return 段落列表
     */
    private List<Paragraph> getParagraphList(Config config, Chapter chapter, HtmlPage htmlPage) {
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
            paragraph.setBookid(chapter.getBookid());
            paragraph.setChapterid(chapter.getChapterid());
            paragraph.setContent(line);
            paragraph.setSeqid((long) i);
            paragraphList.add(paragraph);
        }
        return paragraphList;
    }
}
