package top.niandui.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
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

import static top.niandui.common.uitls.MethodUtil.convert;
import static top.niandui.config.PublicConstant.BOOK_PROXY;
import static top.niandui.config.PublicConstant.DAY_SECOND;
import static top.niandui.utils.HandleUtil.getIsEndHref;
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
    private WebClient getWebClient(Map handleInfo) {
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
            BiFunction<String, String, Boolean> isEndHref = getIsEndHref(handleInfo);
            // 获取开始结束时间
            long startTime = System.currentTimeMillis(), endTimes;
            // 获取起始页面
            HtmlPage htmlPage = getWebClient(handleInfo).getPage(book.getStarturl());
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
                        htmlPage = getWebClient(handleInfo).getPage(htmlPage.getUrl());
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
                    if (!paragraphList.isEmpty()) {
                        // 创建段落列表
                        iParagraphDao.createBatch(paragraphList);
                    }
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
     * @param config  配置信息
     * @param chapter 章节信息
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
            // 获取开始结束时间
            long startTime = System.currentTimeMillis(), endTimes;
            // 获取起始页面
            HtmlPage htmlPage = getWebClient(handleInfo).getPage(chapter.getUrl());
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
                    htmlPage = getWebClient(handleInfo).getPage(htmlPage.getUrl());
                    continue;
                }
                // 计算使用时间
                endTimes = System.currentTimeMillis();
                log.info(title + " " + (endTimes - startTime) / 1000.0 + "s");
                // 删除原有段落信息
                iParagraphDao.deleteByBookAndChapterId(chapter.getBookid(), chapter.getChapterid().toString());
                // 获取段落列表
                List<Paragraph> paragraphList = getParagraphList(config, chapter, htmlPage);
                if (!paragraphList.isEmpty()) {
                    // 创建段落列表
                    iParagraphDao.createBatch(paragraphList);
                }
                break;
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
     * @param config   配置信息
     * @param chapter  章节信息
     * @param htmlPage html页面信息
     * @return 段落列表
     */
    private List<Paragraph> getParagraphList(Config config, Chapter chapter, HtmlPage htmlPage) {
        // 获取内容DOM列表
        List<DomText> list = htmlPage.getByXPath(config.getConmatch());
        // 去除空行后的
        List<String> lineList = list.stream().map(text -> {
            // 去除每一行前面的空字符
            String line = text.toString().trim();
            int start = 0;
            while (line.startsWith("　", start)) {
                start++;
            }
            return line.substring(start).trim();
            // 过滤出有内容的行
        }).filter(StringUtils::hasText).collect(Collectors.toList());
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
}
