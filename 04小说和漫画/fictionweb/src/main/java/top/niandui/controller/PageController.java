package top.niandui.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import springfox.documentation.annotations.ApiIgnore;
import top.niandui.service.IBookService;
import top.niandui.service.IConfigService;

import java.util.Collections;
import java.util.Map;

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
    private IBookService iBookService;
    @Autowired
    private IConfigService iConfigService;

    /*站点*/
    @GetMapping("/site/list")
    public String siteList() {
        return "site/list";
    }

    @GetMapping("/site/add/{id}")
    public String siteAdd(@PathVariable String id, Map map) {
        map.put("id", id);
        addOptionConfig(map);
        return "site/add";
    }

    /*书籍*/
    @GetMapping("/book/list")
    public String bookList() {
        return "book/list";
    }

    @GetMapping("/book/add/{id}")
    public String bookAdd(@PathVariable String id, Map map) {
        map.put("id", id);
        addOptionConfig(map);
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
    @GetMapping("/chapter/list/{id}")
    public String chapterList(@PathVariable String id, Map map) throws Exception {
        map.put("book", iBookService.model(Long.valueOf(id)));
        return "chapter/list";
    }

    @GetMapping("/chapter/add/{id}")
    public String chapterAdd(@PathVariable String id, Map map) {
        map.put("id", id);
        return "chapter/add";
    }

    private void addOptionConfig(Map map) {
        try {
            map.put("optionConfig", iConfigService.option());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString());
            map.put("optionConfig", Collections.EMPTY_LIST);
        }
    }
}
