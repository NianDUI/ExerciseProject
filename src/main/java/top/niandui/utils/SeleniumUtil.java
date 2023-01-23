package top.niandui.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;
import org.springframework.util.StringUtils;
import top.niandui.model.Book;
import top.niandui.model.Chapter;
import top.niandui.model.Config;
import top.niandui.model.Paragraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static top.niandui.utils.TaskStateUtil.getBookTaskStatus;

/**
 * selenium 工具
 *
 * @author liyongda
 * @version 1.0
 * @date 2023/1/15 16:36
 */
@Slf4j
public class SeleniumUtil {

    /**
     * 创建驱动
     *
     * <pre>
     * options.add_argument(‘headless’) # 无头模式
     * options.add_argument(‘window-size={}x{}’.format(width, height)) # 直接配置大小和set_window_size一样
     * options.add_argument(‘disable-gpu’) # 禁用GPU加速
     * options.add_argument(‘proxy-server={}’.format(self.proxy_server)) # 配置代理
     * options.add_argument(’–no-sandbox’) # 沙盒模式运行
     * options.add_argument(’–disable-setuid-sandbox’) # 禁用沙盒
     * options.add_argument(’–disable-dev-shm-usage’) # 大量渲染时候写入/tmp而非/dev/shm
     * options.add_argument(’–user-data-dir={profile_path}’.format(profile_path)) # 用户数据存入指定文件
     * options.add_argument('no-default-browser-check) # 不做浏览器默认检查
     * options.add_argument("–disable-popup-blocking") # 允许弹窗
     * options.add_argument("–disable-extensions") # 禁用扩展
     * options.add_argument("–ignore-certificate-errors") # 忽略不信任证书
     * options.add_argument("–no-first-run") # 初始化时为空白页面
     * options.add_argument(’–start-maximized’) # 最大化启动
     * options.add_argument(’–disable-notifications’) # 禁用通知警告
     * options.add_argument(’–enable-automation’) # 通知(通知用户其浏览器正由自动化测试控制)
     * options.add_argument(’–disable-xss-auditor’) # 禁止xss防护
     * options.add_argument(’–disable-web-security’) # 关闭安全策略
     * options.add_argument(’–allow-running-insecure-content’) # 允许运行不安全的内容
     * options.add_argument(’–disable-webgl’) # 禁用webgl
     * options.add_argument(’–homedir={}’) # 指定主目录存放位置
     * options.add_argument(’–disk-cache-dir={临时文件目录}’) # 指定临时文件目录
     * options.add_argument(‘disable-cache’) # 禁用缓存
     * options.add_argument(‘excludeSwitches’, [‘enable-automation’]) # 开发者模式
     * ————————————————
     * 版权声明：本文为CSDN博主「四个现代化」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
     * 原文链接：https://blog.csdn.net/qq254271304/article/details/105766653
     * </pre>
     *
     * @return web驱动
     */
    private static WebDriver newDriver() {
        // 加载驱动
//        WebDriverManager.firefoxdriver().setup();
        WebDriverManager.edgedriver().setup();
        // 创建驱动
//        return new FirefoxDriver();
        EdgeOptions edgeOptions = new EdgeOptions();
        // 无头模式 不用打开图形界面
        edgeOptions.addArguments("--headless");
        // 禁用GPU加速
        edgeOptions.addArguments("--disable-gpu");
        // 盒模式运行 Chrome在root权限下跑
        edgeOptions.addArguments("--no-sandbox");
        // 大量渲染时候写入/tmp而非/dev/shm
        edgeOptions.addArguments("--disable-dev-shm-usage");
        // 禁用扩展
        edgeOptions.addArguments("--disable-extensions");
        // 忽略不信任证书
        edgeOptions.addArguments("-–ignore-certificate-errors");
        // 不做浏览器默认检查
        edgeOptions.addArguments("no-default-browser-check");
        return new EdgeDriver(edgeOptions);
    }

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
        // 创建驱动
        WebDriver driver = newDriver();
        try {
            // 获取开始结束时间
            long startTime = System.currentTimeMillis(), endTimes;
            // 获取根uri
            String rootUri = HandleUtil.getRootUri(book.getStarturl());
            // 获取起始页面
            driver.get(book.getStarturl());
            int errorNum = 0;
            while (getBookTaskStatus(book.getBookid()) != 0) {
                String url = driver.getCurrentUrl();
                JXDocument jxDocument = JXDocument.create(driver.getPageSource());
                if (!isFirstJump) {
                    Chapter chapter = new Chapter();
                    // 获取标题DOM列表
                    List<JXNode> titleList = jxDocument.selN(config.getTitlematch());
                    // 调用自定义方法处理标题
                    try {
                        chapter.setRawname(titleList.get(0).asString().trim());
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
                        driver.get(url);
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
                    List<Paragraph> paragraphList = getParagraphList(config, chapter, jxDocument);
                    // 创建段落列表
                    webClientUtil.createBatchParagraph(paragraphList);
                }
                if (isFirstJump) {
                    isFirstJump = false;
                }
                // 获取跳转超链接DOM列表
                List<JXNode> aList = jxDocument.selN(config.getAmatch());
                // 获取下一页的超链接DOM
                JXNode next = aList.get(config.getNexta());
                String nextHref = next.asElement().attr("href").trim();
                // 调用自定义方法判断下一页是否还有内容
                if (isEndHref.apply(url, nextHref)) {
                    break;
                }
                // 调用休眠处理方法
                HandleUtil.sleepHandler.get();
                // 跳转下一页
                String nextUrl;
                if (nextHref.contains("http") && nextHref.contains("://")) {
                    // 下一页url：http://www.77dushu.la/chapter/105444/44091664.html
                    nextUrl = nextHref;
                } else {
                    // 下一页url：/chapter/105444/44091664.html
                    nextUrl = rootUri + nextHref;
                }
                driver.get(nextUrl);
            }
        } finally {
            driver.quit();
        }
    }

    /**
     * 获取该章节段落内容
     *
     * @param config  配置信息
     * @param chapter 章节信息
     * @param handleInfo 处理信息
     */
    public static void getChapter(WebClientUtil webClientUtil, Config config, Chapter chapter, Map handleInfo) throws Exception {
        // 创建驱动
        WebDriver driver = newDriver();
        try {
            // 获取开始结束时间
            long startTime = System.currentTimeMillis(), endTimes;
            // 获取起始页面
            String url = chapter.getUrl();
            driver.get(url);
            int num = 0;
            while (num++ < 10) {
                JXDocument jxDocument = JXDocument.create(driver.getPageSource());
                // 获取标题DOM列表
                List<JXNode> titleList = jxDocument.selN(config.getTitlematch());
                // 标题是否存在
                String title;
                try {
                    title = titleList.get(0).asString().trim();
                } catch (Exception e) {
                    // 获取内容出错时，为服务端限制，重新拉去该页面。
                    // Index: 0, Size: 0
                    log.info(e.getMessage());
                    // 调用休眠处理方法
                    HandleUtil.sleepHandler.get();
                    startTime = System.currentTimeMillis();
                    driver.get(url);
                    continue;
                }
                // 计算使用时间
                endTimes = System.currentTimeMillis();
                log.info(title + " " + (endTimes - startTime) / 1000.0 + "s");
                // 删除原有段落信息
                webClientUtil.deleteParagraphByBookAndChapterId(chapter.getBookid(), chapter.getChapterid().toString());
                // 获取段落列表
                List<Paragraph> paragraphList = getParagraphList(config, chapter, jxDocument);
                // 创建段落列表
                webClientUtil.createBatchParagraph(paragraphList);
                break;
            }
        } finally {
            driver.quit();
        }
    }

    /**
     * 获取段落列表
     *
     * @param config     配置信息
     * @param chapter    章节信息
     * @param jxDocument html页面信息
     * @return 段落列表
     */
    private static List<Paragraph> getParagraphList(Config config, Chapter chapter, JXDocument jxDocument) {
        // 获取内容DOM列表
        List<JXNode> list = jxDocument.selN(config.getConmatch());
        // 去除空行后的
        List<String> lineList = list.stream().map(text -> {
            // 去除每一行前面的空字符
            String line = text.asString().trim();
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
