package top.niandui.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.niandui.common.base.BaseController;
import top.niandui.common.model.IdNameModel;
import top.niandui.common.model.PageList;
import top.niandui.common.model.ResponseData;
import top.niandui.model.Chapter;
import top.niandui.model.vo.ChapterListReturnVO;
import top.niandui.model.vo.ChapterSearchVO;
import top.niandui.service.IChapterService;

import java.util.List;

/**
 * @author 李永达
 * @version 1.0
 * @title ChapterController.java
 * @description 章节
 * @time: 2020/4/6 11:11
 */
@RestController
@Api(tags = "章节")
@RequestMapping("/api")
public class ChapterController extends BaseController {
    @Autowired
    private IChapterService iChapterService;

    @PostMapping("/saveChapter")
    @ApiOperation(value = "保存章节", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>无id为新增,有id为修改")
    public ResponseData saveChapter(@RequestBody @Validated Chapter chapter) throws Exception {
        if (chapter.getChapterid() == null) {
            iChapterService.create(chapter);
        } else {
            iChapterService.update(chapter);
        }
        return ResponseData.ok();
    }

    @PostMapping("/checkChapterName")
    @ApiOperation(value = "章节重名校验", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>")
    public ResponseData checkChapterName(@RequestBody @Validated IdNameModel checkName) throws Exception {
        iChapterService.checkName(checkName);
        return ResponseData.ok();
    }

    @GetMapping("/modelChapter/{id}")
    @ApiOperation(value = "查询单个章节", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "章节id", dataType = "Long", required = true))
    public ResponseData<Chapter> modelChapter(@PathVariable Long id) throws Exception {
        return ResponseData.ok(iChapterService.model(id));
    }

    @PostMapping("/queryChapterList")
    @ApiOperation(value = "查询章节列表", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>")
    public ResponseData<PageList<ChapterListReturnVO>> queryChapterList(@RequestBody @Validated ChapterSearchVO chapterSearchVO) throws Exception {
        return ResponseData.ok(iChapterService.queryList(chapterSearchVO));
    }

    @GetMapping("/optionChapter")
    @ApiOperation(value = "章节下拉", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>")
    public ResponseData<List<IdNameModel>> optionChapter() throws Exception {
        return ResponseData.ok(iChapterService.option());
    }

    @GetMapping("/deleteChapter/{id}")
    @ApiOperation(value = "删除章节", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "章节id,多个用逗号隔开", dataType = "String", required = true))
    public ResponseData deleteChapter(@PathVariable String id) throws Exception {
        iChapterService.delete(id);
        return ResponseData.ok();
    }

    @GetMapping("/obtainChapter/{id}")
    @ApiOperation(value = "获取章节", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "书籍id", dataType = "Long", required = true))
    public ResponseData obtainChapter(@PathVariable Long id) throws Exception {
        iChapterService.obtainChapter(id);
        return ResponseData.ok();
    }
}
