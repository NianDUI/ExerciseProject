package top.niandui.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
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
import top.niandui.model.vo.SpecifiedFollowUpGetVO;
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
@ApiSupport(author = "李永达")
public class ChapterController extends BaseController {
    @Autowired
    private IChapterService iChapterService;

    @PostMapping("/saveChapter")
    @ApiOperation(value = "保存章节", notes = "时间：2020/04/06<br>无id为新增,有id为修改")
    public ResponseData saveChapter(@RequestBody @Validated Chapter chapter) throws Exception {
        if (chapter.getChapterid() == null) {
            iChapterService.create(chapter);
        } else {
            iChapterService.update(chapter);
        }
        return ResponseData.ok();
    }

    @PostMapping("/checkChapterName")
    @ApiOperation(value = "章节重名校验", notes = "时间：2020/04/06")
    public ResponseData checkChapterName(@RequestBody @Validated IdNameModel<Long> checkName) throws Exception {
        iChapterService.checkName(checkName);
        return ResponseData.ok();
    }

    @GetMapping("/modelChapter/{id}")
    @ApiOperation(value = "查询单个章节", notes = "时间：2020/04/06")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "章节id", dataType = "Long", required = true))
    public ResponseData<Chapter> modelChapter(@PathVariable Long id) throws Exception {
        return ResponseData.ok(iChapterService.model(id));
    }

    @PostMapping("/queryChapterList")
    @ApiOperation(value = "查询章节列表", notes = "时间：2020/04/06")
    public ResponseData<PageList<ChapterListReturnVO>> queryChapterList(@RequestBody @Validated ChapterSearchVO chapterSearchVO) throws Exception {
        return ResponseData.ok(iChapterService.queryList(chapterSearchVO));
    }

    @GetMapping("/optionChapter")
    @ApiOperation(value = "章节下拉", notes = "时间：2020/04/06")
    public ResponseData<List<IdNameModel<Long>>> optionChapter() throws Exception {
        return ResponseData.ok(iChapterService.option());
    }

    @GetMapping("/deleteChapter/{id}")
    @ApiOperation(value = "删除章节", notes = "时间：2020/04/06")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "章节id,多个用逗号隔开", dataType = "String", required = true))
    public ResponseData deleteChapter(@PathVariable String id) throws Exception {
        iChapterService.delete(id);
        return ResponseData.ok();
    }

    @GetMapping("/reacquireAllChapter/{id}")
    @ApiOperation(value = "重新获取所有章节", notes = "时间：2020/04/06")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "书籍id", dataType = "Long", required = true))
    public ResponseData reacquireAllChapter(@PathVariable Long id) throws Exception {
        iChapterService.reacquireAllChapter(id);
        return ResponseData.ok();
    }

    @GetMapping("/getFollowUpChapter/{id}")
    @ApiOperation(value = "获取后续章节", notes = "时间：2020/04/07")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "书籍id", dataType = "Long", required = true))
    public ResponseData getFollowUpChapter(@PathVariable Long id) throws Exception {
        iChapterService.getFollowUpChapter(id);
        return ResponseData.ok();
    }

    @GetMapping("/reacquireSingleChapter/{id}")
    @ApiOperation(value = "重新获取单个章节", notes = "时间：2020/04/10")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "章节id", dataType = "Long", required = true))
    public ResponseData reacquireSingleChapter(@PathVariable Long id) throws Exception {
        iChapterService.reacquireSingleChapter(id);
        return ResponseData.ok();
    }

    @PostMapping("/getSpecifiedAndFollowUpChapter")
    @ApiOperation(value = "获取指定章节和后续章节", notes = "时间：2020/04/07")
    public ResponseData getSpecifiedAndFollowUpChapter(@RequestBody @Validated SpecifiedFollowUpGetVO getVO) throws Exception {
        iChapterService.getSpecifiedAndFollowUpChapter(getVO);
        return ResponseData.ok();
    }

    @GetMapping("/queryGetStatus/{id}")
    @ApiOperation(value = "查询获取状态", notes = "时间：2020/06/27")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "书籍id", dataType = "Long", required = true))
    public ResponseData<Integer> queryGetStatus(@PathVariable Long id) throws Exception {
        return ResponseData.ok(iChapterService.queryGetStatus(id));
    }

    @GetMapping("/stopGet/{id}")
    @ApiOperation(value = "停止获取", notes = "时间：2020/06/27")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "书籍id", dataType = "Long", required = true))
    public ResponseData stopGet(@PathVariable Long id) throws Exception {
        iChapterService.stopGet(id);
        return ResponseData.ok();
    }
}
