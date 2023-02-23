package top.niandui.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.niandui.common.base.BaseController;
import top.niandui.common.model.IdNameModel;
import top.niandui.common.model.PageList;
import top.niandui.common.model.ResponseData;
import top.niandui.model.Chapter;
import top.niandui.model.vo.ChapterInfoReturnVO;
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
@Tag(name = "章节")
@RequestMapping("/api")
@ApiSupport(author = "李永达")
public class ChapterController extends BaseController {
    @Autowired
    private IChapterService iChapterService;

    @PostMapping("/saveChapter")
    @Operation(summary = "保存章节", description = "时间：2020/04/06<br>无id为新增,有id为修改")
    public ResponseData saveChapter(@RequestBody @Validated Chapter chapter) throws Exception {
        if (chapter.getChapterid() == null) {
            iChapterService.create(chapter);
        } else {
            iChapterService.update(chapter);
        }
        return ResponseData.ok();
    }

    @PostMapping("/checkChapterName")
    @Operation(summary = "章节重名校验", description = "时间：2020/04/06")
    public ResponseData checkChapterName(@RequestBody @Validated IdNameModel<Long> checkName) throws Exception {
        iChapterService.checkName(checkName);
        return ResponseData.ok();
    }

    @GetMapping("/modelChapter/{id}")
    @Operation(summary = "查询单个章节", description = "时间：2020/04/06")
    @Parameters(@Parameter(name = "id", description = "章节id", required = true))
    public ResponseData<Chapter> modelChapter(@PathVariable Long id) throws Exception {
        return ResponseData.ok(iChapterService.model(id));
    }

    @PostMapping("/queryChapterList")
    @Operation(summary = "查询章节列表", description = "时间：2020/04/06")
    public ResponseData<PageList<ChapterListReturnVO>> queryChapterList(@RequestBody @Validated ChapterSearchVO chapterSearchVO) throws Exception {
        return ResponseData.ok(iChapterService.queryList(chapterSearchVO));
    }

    @GetMapping("/optionChapter")
    @Operation(summary = "章节下拉", description = "时间：2020/04/06")
    public ResponseData<List<IdNameModel<Long>>> optionChapter() throws Exception {
        return ResponseData.ok(iChapterService.option());
    }

    @GetMapping("/deleteChapter/{id}")
    @Operation(summary = "删除章节", description = "时间：2020/04/06")
    @Parameters(@Parameter(name = "id", description = "章节id,多个用逗号隔开", required = true))
    public ResponseData deleteChapter(@PathVariable String id) throws Exception {
        iChapterService.delete(id);
        return ResponseData.ok();
    }

    @GetMapping("/reacquireAllChapter/{id}")
    @Operation(summary = "重新获取所有章节", description = "时间：2020/04/06")
    @Parameters(@Parameter(name = "id", description = "书籍id", required = true))
    public ResponseData reacquireAllChapter(@PathVariable Long id) throws Exception {
        iChapterService.reacquireAllChapter(id);
        return ResponseData.ok();
    }

    @GetMapping("/getFollowUpChapter/{id}")
    @Operation(summary = "获取后续章节", description = "时间：2020/04/07")
    @Parameters(@Parameter(name = "id", description = "书籍id", required = true))
    public ResponseData getFollowUpChapter(@PathVariable Long id) throws Exception {
        iChapterService.getFollowUpChapter(id);
        return ResponseData.ok();
    }

    @GetMapping("/reacquireSingleChapter/{id}")
    @Operation(summary = "重新获取单个章节", description = "时间：2020/04/10")
    @Parameters(@Parameter(name = "id", description = "章节id", required = true))
    public ResponseData reacquireSingleChapter(@PathVariable Long id) throws Exception {
        iChapterService.reacquireSingleChapter(id);
        return ResponseData.ok();
    }

    @GetMapping("/queryChapterInfo/{id}")
    @Operation(summary = "查询章节信息", description = "时间：2021/02/24")
    @Parameters(@Parameter(name = "id", description = "章节id", required = true))
    public ResponseData<ChapterInfoReturnVO> queryChapterInfo(@PathVariable Long id) throws Exception {
        return ResponseData.ok(iChapterService.queryChapterInfo(id));
    }

    @PostMapping("/getSpecifiedAndFollowUpChapter")
    @Operation(summary = "获取指定章节和后续章节", description = "时间：2020/04/07")
    public ResponseData getSpecifiedAndFollowUpChapter(@RequestBody @Validated SpecifiedFollowUpGetVO getVO) throws Exception {
        iChapterService.getSpecifiedAndFollowUpChapter(getVO);
        return ResponseData.ok();
    }

    @GetMapping("/queryGetStatus/{id}")
    @Operation(summary = "查询获取状态", description = "时间：2020/06/27")
    @Parameters(@Parameter(name = "id", description = "书籍id", required = true))
    public ResponseData<Integer> queryGetStatus(@PathVariable Long id) throws Exception {
        return ResponseData.ok(iChapterService.queryGetStatus(id));
    }

    @GetMapping("/stopGet/{id}")
    @Operation(summary = "停止获取", description = "时间：2020/06/27")
    @Parameters(@Parameter(name = "id", description = "书籍id", required = true))
    public ResponseData stopGet(@PathVariable Long id) throws Exception {
        iChapterService.stopGet(id);
        return ResponseData.ok();
    }
}
