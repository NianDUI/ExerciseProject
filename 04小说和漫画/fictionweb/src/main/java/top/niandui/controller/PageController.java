package top.niandui.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import springfox.documentation.annotations.ApiIgnore;
import top.niandui.config.ConfigInfo;
import top.niandui.model.vo.ChapterInfoReturnVO;
import top.niandui.service.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Map;

import static top.niandui.utils.PathUtil.getPath;

/**
 * @Title: PageController.java
 * @description: 视图管理
 * @time: 2020/3/28 16:20
 * @author: liyongda
 * @version: 1.0
 */
@Slf4j
@ApiIgnore
@Controller
public class PageController {
    @Autowired
    private IConfigService iConfigService;
    @Autowired
    private ISiteService iSiteService;
    @Autowired
    private IBookService iBookService;
    @Autowired
    private IChapterService iChapterService;
    @Autowired
    private IFileService iFileService;
    @Autowired
    private ConfigInfo configInfo;

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    /*站点*/
    @GetMapping("/site/list")
    public String siteList() {
        return "site/list";
    }

    @SneakyThrows
    @GetMapping("/site/add/{id}")
    public String siteAdd(@PathVariable String id, Map map) {
        map.put("id", id);
        map.put("optionConfig", iConfigService.option());
        return "site/add";
    }

    /*书籍*/
    @GetMapping("/book/list/{id}")
    public String bookList(@PathVariable String id, Map map) {
        map.put("siteid", id);
        return "book/list";
    }

    @GetMapping("/book/list")
    public String bookList(Map map) {
        map.put("siteid", "null");
        return "book/list";
    }

    @SneakyThrows
    @GetMapping("/book/add/{id}")
    public String bookAdd(@PathVariable String id, Map map) {
        map.put("id", id);
        map.put("optionSite", iSiteService.option());
        map.put("optionConfig", iConfigService.option());
        return "book/add";
    }

    /*配置*/
    @GetMapping("/config/list")
    public String configList() {
        return "config/list";
    }

    @GetMapping("/config/add/{id}")
    public String configAdd(@PathVariable String id, Map map) {
        map.put("id", id);
        return "config/add";
    }

    /*章节*/
    @SneakyThrows
    @GetMapping("/chapter/list/{id}")
    public String chapterList(@PathVariable String id, Map map) {
        map.put("book", iBookService.model(Long.valueOf(id)));
        return "chapter/list";
    }

    @SneakyThrows
    @GetMapping("/chapter/add/{id}")
    public String chapterAdd(@PathVariable String id, Map map) {
        map.put("id", id);
        map.put("optionBook", iBookService.option());
        map.put("optionConfig", iConfigService.option());
        return "chapter/add";
    }

    @SneakyThrows
    @GetMapping("/chapter/show/{id}")
    public String chapterShow(@PathVariable String id, Map map) {
        ChapterInfoReturnVO rv = iChapterService.queryChapterInfo(Long.valueOf(id));
        map.put("book", rv.getBook());
        map.put("chapter", rv.getChapter());
        map.put("previd", rv.getPrevid() + "");
        map.put("nextid", rv.getNextid() + "");
        map.put("paragraphList", rv.getParagraphList());
        return "chapter/show";
    }

    /*文件*/
    @SneakyThrows
    @GetMapping("/file/list/**")
    public String fileList(HttpServletRequest request, Map map) {
        String[] paths = getPath(request, "list");
        String path = paths[1];
        File file = new File(configInfo.getFilePath() + path);
        if (file.isFile()) {
            String name = file.getName();
            map.put("name", name);
            int i = name.lastIndexOf(".");
            if (i > 0) {
                map.put("form", name.substring(i + 1).toLowerCase());
            } else {
                map.put("form", name);
            }
            map.put("path", paths[0]);
            return "file/video";
        } else {
            map.put("list", iFileService.list(path));
            map.put("pathName", path);
            map.put("path", paths[0]);
            return "file/list";
        }
    }

    @SneakyThrows
    @GetMapping("/file/video1")
    public String fileVideo() {
        return "file/video1";
    }
}
