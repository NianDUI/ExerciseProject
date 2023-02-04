package top.niandui.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.niandui.common.expection.ReStateException;
import top.niandui.common.uitls.redis.RedisUtil;
import top.niandui.config.AsyncConfig;
import top.niandui.dao.IBookDao;
import top.niandui.dao.IChapterDao;
import top.niandui.dao.IParagraphDao;
import top.niandui.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static top.niandui.common.uitls.MethodUtil.convert;
import static top.niandui.config.PublicConstant.BOOK_PROXY;
import static top.niandui.config.PublicConstant.DAY_SECOND;
import static top.niandui.utils.TaskStateUtil.*;

/**
 * Web客户端工具
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/1/17 11:00
 */
@Slf4j
@Component
public class WebClientUtil {
    @Autowired
    private ObjectMapper json;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IBookDao iBookDao;
    @Autowired
    private IChapterDao iChapterDao;
    @Autowired
    private IParagraphDao iParagraphDao;

    // 获取log对象
    public static Logger getLog() {
        return log;
    }

    // 新建一个模拟谷歌Chrome浏览器的浏览器客户端对象
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

    // 获取一个新的浏览器客户端对象，并判断是否设置
    WebClient getWebClient(Map handleInfo) {
        WebClient client = getWebClient();
        Integer proxyid = convert(handleInfo.get("proxyid"), Integer::valueOf, Integer.class);
        if (proxyid != null && proxyid != 0) {
            // 获取书籍代理缓存
            String key = BOOK_PROXY + handleInfo.get("bookid");
            Proxy proxy = (Proxy) redisUtil.get(key);
            boolean isQuery = false;
            if (proxy == null) {
                proxy = iBookDao.modelProxy(proxyid);
                isQuery = true;
            }
            if (proxy != null) {
                ProxyConfig proxyConfig = client.getOptions().getProxyConfig();
                proxyConfig.setSocksProxy(proxy.getType() == 1);
                proxyConfig.setProxyHost(proxy.getHost());
                proxyConfig.setProxyPort(proxy.getPort());
                if (isQuery) {
                    // 是查询出来的存入缓存
                    redisUtil.set(key, proxy, DAY_SECOND);
                }
            }
        }
        return client;
    }

    /**
     * 获取书籍各章节内容
     *
     * @param config      配置信息
     * @param book        书籍信息
     * @param seqid       章节序号
     * @param isFirstJump 第一次是否跳过,t跳过、f不跳过
     */
    @Async(AsyncConfig.GET_BOOK_TASK_EXECUTOR_BEAN_NAME)
    public void getBook(Config config, Book book, long seqid, boolean isFirstJump) {
        try {
            // 更新任务状态
            if (updateTaskStatusByRawStatus(book.getBookid(), book.getTaskstatus(), 0) == 0) {
                log.info(book.getName() + getTaskStatus(book.getTaskstatus()) + "已被其他服务处理");
            }
            Map handleInfo = json.readValue(book.getHandlerinfo(), Map.class);
            handleInfo.put("bookid", book.getBookid());
            Function<String, String> titleHandler = HandleUtil.getTitleHandler(handleInfo);
            BiFunction<String, String, Boolean> isEndHref = HandleUtil.getIsEndHref(handleInfo);
            // 判断获取工具
            if ("selenium".equals(handleInfo.get("getUtil"))) {
                // selenium 获取
                SeleniumUtil.getBook(this, config, book, seqid, isFirstJump, handleInfo, titleHandler, isEndHref);
            } else {
                // htmlunit 获取
                HtmlunitUtil.getBook(this, config, book, seqid, isFirstJump, handleInfo, titleHandler, isEndHref);
            }
            log.info("获取结束...");
        } catch (Exception e) {
            log.error("获取失败...", e);
        } finally {
            // 更新任务状态
            updateBookTaskStatus(book.getBookid(), 0);
            // 删除书籍代理缓存
            redisUtil.del(BOOK_PROXY + book.getBookid());
        }
    }

    /**
     * 获取该章节段落内容
     *
     * @param config      配置信息
     * @param chapter     章节信息
     * @param handlerinfo 处理信息
     */
    public void getChapter(Config config, Chapter chapter, String handlerinfo) {
        try {
            // 更新任务状态
            if (updateTaskStatusByRawStatus(chapter.getBookid(), 3, 0) == 0) {
                log.info(chapter.getName() + getTaskStatus(3) + "已被其他服务处理");
            }
            Map handleInfo = json.readValue(handlerinfo, Map.class);
            handleInfo.put("bookid", chapter.getBookid());
            // 判断获取工具
            if ("selenium".equals(handleInfo.get("getUtil"))) {
                // selenium 获取
                SeleniumUtil.getChapter(this, config, chapter, handleInfo);
            } else {
                // htmlunit 获取
                HtmlunitUtil.getChapter(this, config, chapter, handleInfo);
            }
            log.info("获取结束...");
        } catch (Exception e) {
            throw new ReStateException("获取失败", e);
        } finally {
            // 更新任务状态
            updateBookTaskStatus(chapter.getBookid(), 0);
            // 删除书籍代理缓存
            redisUtil.del(BOOK_PROXY + chapter.getBookid());
        }
    }

    /**
     * 获取段落列表
     *
     * @param config     配置信息
     * @param chapter    章节信息
     * @param listStream 段落流
     * @return 段落列表
     */
    public static List<Paragraph> getParagraphList(Config config, Chapter chapter, Stream<String> listStream) {
        // 去除空行后的
        List<String> lineList = listStream.map(line -> {
            // 去除每一行前面的空字符
            line = line.trim();
            int start = 0;
            while (line.startsWith("　", start)) {
                start++;
            }
            return line.substring(start).trim();
            // 过滤出有内容的行
        }).filter(StringUtils::hasText)
                // 过滤出段落内容：不是以"【"开头，并且不包含"换源App" 的
                .filter(line -> !line.startsWith("【") && !line.contains("换源App"))
                .collect(Collectors.toList());
        // 生成行对象列表
        List<Paragraph> paragraphList = new ArrayList<>();
        long seqid = 0;
        int end = lineList.size() + config.getEndoffset();
        for (int i = config.getStartoffset(); i < end; i++, seqid++) {
            Paragraph paragraph = new Paragraph();
            paragraph.setBookid(chapter.getBookid());
            paragraph.setChapterid(chapter.getChapterid());
            paragraph.setContent(lineList.get(i));
            paragraph.setSeqid(seqid);
            paragraphList.add(paragraph);
        }
        return paragraphList;
    }

    /**
     * 创建章节
     *
     * @param chapter 章节
     * @return 数量
     */
    public int createChapter(Chapter chapter) {
        // 创建章节
        return iChapterDao.create(chapter);
    }

    /**
     * 创建段落列表
     *
     * @param paragraphList 段落列表
     * @return 数量
     */
    public int createBatchParagraph(List<Paragraph> paragraphList) {
        if (!paragraphList.isEmpty()) {
            // 创建段落列表
            return iParagraphDao.createBatch(paragraphList);
        }
        return -1;
    }

    /**
     * 通过书籍和章节id删除段落
     *
     * @param bookid    书籍id
     * @param chapterid 章节id
     */
    public void deleteParagraphByBookAndChapterId(Long bookid, String chapterid) {
        // 删除段落信息
        iParagraphDao.deleteByBookAndChapterId(bookid, chapterid);
    }
}
