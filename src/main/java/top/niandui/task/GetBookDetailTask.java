package top.niandui.task;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.niandui.common.model.IdNameModel;
import top.niandui.dao.IBookDao;
import top.niandui.utils.WebClientUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 获取书籍详情任务
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/5/10 11:00
 */
@Slf4j
@Component
public class GetBookDetailTask implements Runnable {
    // 下拉书籍参数
    private static final Map PARAMS = Collections.singletonMap("taskswitch", 1);
    // 起点书籍搜索url
    private static final String QI_DIAN_SEARCH_URL = "https://www.qidian.com/search?kw=";
    // XPath：搜索第一个书籍项
    private static final String XPATH_BOOK_ITEM = "//li[@class='res-book-item' and @data-rid='1']";
    // XPath：书籍超链接信息
    private static final String XPATH_BOOK_ANCHOR = ".//div[@class='book-mid-info']/h4/a";
    // XPath：书籍作者相关信息
    private static final String XPATH_BOOK_AUTHOR = ".//div[@class='book-mid-info']/p[@class='author']";
    // XPath：书籍更新信息
    private static final String XPATH_BOOK_UPDATE = ".//div[@class='book-mid-info']/p[@class='update']";
    // 是否启用
    private final boolean enable = true;
    @Autowired
    private IBookDao iBookDao;

    @Override
    public void run() {
        if (enable) {
            try {
                getBookDetail();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    // 获取书籍详情
    public void getBookDetail() {
        log.info("定时任务启动(获取书籍详情)...");
        List<IdNameModel<Long>> option = iBookDao.option(PARAMS);
        for (IdNameModel<Long> idNameModel : option) {
            String name = idNameModel.getName();
            try {
                // 获取一个web客户端
                WebClient client = WebClientUtil.getWebClient();
                // 获取搜索页面
                HtmlPage page = client.getPage(QI_DIAN_SEARCH_URL + name);
                // 筛选出第一个搜索结果项
                HtmlListItem resBookItem = page.getFirstByXPath(XPATH_BOOK_ITEM);
                // 获取结果项中书籍超链接
                HtmlAnchor bookAnchor = resBookItem.getFirstByXPath(XPATH_BOOK_ANCHOR);
                if (!name.equals(bookAnchor.asNormalizedText().trim())) {
                    continue;
                }
                // 获取结果项中作者和状态信息
                HtmlParagraph author = resBookItem.getFirstByXPath(XPATH_BOOK_AUTHOR);
                String autorText = author.asNormalizedText().trim();
                // 获取结果项中章节更新信息
                HtmlParagraph update = resBookItem.getFirstByXPath(XPATH_BOOK_UPDATE);
                String updateText = update.asNormalizedText().trim();
                // 组装书籍详情
                String detail = new StringBuilder(autorText)
                        .append("\n").append(updateText)
                        // https://book.qidian.com/info/1021420638
                        .append("\n").append("https:").append(bookAnchor.getHrefAttribute())
                        .toString();
                // 更新书籍详情
                iBookDao.updateDetail(idNameModel.getId(), detail);
                if (autorText.contains("|完结")) {
                    // 更新书籍任务开关为关闭
                    iBookDao.updateTaskSwitch(idNameModel.getId(), 0);
                }
            } catch (Exception e) {
                log.error(String.format("获取 %s 书籍详情错误！", name), e);
            }
        }
        log.info("定时任务结束(获取书籍详情)。");
    }
}
