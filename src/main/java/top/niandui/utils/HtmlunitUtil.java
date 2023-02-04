package top.niandui.utils;

import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import top.niandui.model.Book;
import top.niandui.model.Chapter;
import top.niandui.model.Config;
import top.niandui.model.Paragraph;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static top.niandui.utils.TaskStateUtil.getBookTaskStatus;

/**
 * htmlunit 工具
 *
 * @author liyongda
 * @version 1.0
 * @date 2023/1/15 16:36
 */
@Slf4j
public class HtmlunitUtil {

    /**
     * 获取书籍各章节内容
     *
     * @param config       配置信息
     * @param book         书籍信息
     * @param seqid        章节序号
     * @param isFirstJump  第一次是否跳过,t跳过、f不跳过
     * @param handleInfo   处理信息
     * @param titleHandler 标题处理器
     * @param isEndHref    是否为最后一页判断器
     */
    public static void getBook(WebClientUtil webClientUtil, Config config, Book book, long seqid, boolean isFirstJump
            , Map handleInfo, Function<String, String> titleHandler, BiFunction<String, String, Boolean> isEndHref) throws Exception {
        // 获取开始结束时间
        long startTime = System.currentTimeMillis(), endTimes;
        // 获取起始页面
        HtmlPage htmlPage = webClientUtil.getWebClient(handleInfo).getPage(book.getStarturl());
        int errorNum = 0;
        while (getBookTaskStatus(book.getBookid()) != 0) {
            String url = htmlPage.getUrl().toString().trim();
            if (!isFirstJump) {
                Chapter chapter = new Chapter();
                // 获取标题DOM列表
                List<DomText> titleList = htmlPage.getByXPath(config.getTitlematch());
                // 调用自定义方法处理标题
                try {
                    chapter.setRawname(titleList.get(0).toString().trim());
                    // 章节序号
                    handleInfo.put("seqNum", seqid + 1);
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
                    HandleUtil.sleepHandler.get();
                    startTime = System.currentTimeMillis();
                    htmlPage = webClientUtil.getWebClient(handleInfo).getPage(htmlPage.getUrl());
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
                webClientUtil.createChapter(chapter);
                // 获取段落列表
                List<Paragraph> paragraphList = getParagraphList(config, chapter, htmlPage);
                // 创建段落列表
                webClientUtil.createBatchParagraph(paragraphList);
            }
            if (isFirstJump) {
                isFirstJump = false;
            }
            // 获取跳转超链接DOM列表
            List<HtmlAnchor> aList = htmlPage.getByXPath(config.getAmatch());
            // 获取下一页的超链接DOM
            HtmlAnchor next = aList.get(config.getNexta());
            // 调用自定义方法判断下一页是否还有内容
            if (isEndHref.apply(url, next.getHrefAttribute().trim())) {
                break;
            }
            // 跳转下一页
            htmlPage = next.click();
        }
    }

    /**
     * 获取该章节段落内容
     *
     * @param config     配置信息
     * @param chapter    章节信息
     * @param handleInfo 处理信息
     */
    public static void getChapter(WebClientUtil webClientUtil, Config config, Chapter chapter , Map handleInfo) throws Exception {
        // 获取开始结束时间
        long startTime = System.currentTimeMillis(), endTimes;
        // 获取起始页面
        HtmlPage htmlPage = webClientUtil.getWebClient(handleInfo).getPage(chapter.getUrl());
        int num = 0;
        while (num++ < 10) {
            // 获取标题DOM列表
            List<DomText> titleList = htmlPage.getByXPath(config.getTitlematch());
            // 标题是否存在
            String title;
            try {
                title = titleList.get(0).toString().trim();
            } catch (Exception e) {
                // 获取内容出错时，为服务端限制，重新拉去该页面。
                // Index: 0, Size: 0
                log.info(e.getMessage());
                // 调用休眠处理方法
                HandleUtil.sleepHandler.get();
                startTime = System.currentTimeMillis();
                htmlPage = webClientUtil.getWebClient(handleInfo).getPage(htmlPage.getUrl());
                continue;
            }
            // 计算使用时间
            endTimes = System.currentTimeMillis();
            log.info(title + " " + (endTimes - startTime) / 1000.0 + "s");
            // 删除原有段落信息
            webClientUtil.deleteParagraphByBookAndChapterId(chapter.getBookid(), chapter.getChapterid().toString());
            // 获取段落列表
            List<Paragraph> paragraphList = getParagraphList(config, chapter, htmlPage);
            // 创建段落列表
            webClientUtil.createBatchParagraph(paragraphList);
            break;
        }
    }

    /**
     * 获取段落列表
     *
     * @param config   配置信息
     * @param chapter  章节信息
     * @param htmlPage html页面信息
     * @return 段落列表
     */
    private static List<Paragraph> getParagraphList(Config config, Chapter chapter, HtmlPage htmlPage) {
        // 获取内容DOM列表
        List<DomText> list = htmlPage.getByXPath(config.getConmatch());
        Stream<String> listStream = list.stream().map(DomText::toString);
        // 获取段落列表，生成行对象列表
        return WebClientUtil.getParagraphList(config, chapter, listStream);
    }
}
