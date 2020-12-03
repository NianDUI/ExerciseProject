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
import top.niandui.model.Site;
import top.niandui.model.vo.SiteListReturnVO;
import top.niandui.model.vo.SiteSearchVO;
import top.niandui.service.ISiteService;

import java.util.List;

/**
 * @author 李永达
 * @version 1.0
 * @title SiteController.java
 * @description 站点
 * @time 2020/03/22 16:18:53
 */
@RestController
@Api(tags = "站点")
@RequestMapping("/api")
@ApiSupport(author = "李永达")
public class SiteController extends BaseController {
    @Autowired
    private ISiteService iSiteService;

    @PostMapping("/saveSite")
    @ApiOperation(value = "保存站点", notes = "时间：2020/03/22<br>无id为新增,有id为修改")
    public ResponseData saveSite(@RequestBody @Validated Site site) throws Exception {
        if (site.getSiteid() == null) {
            iSiteService.create(site);
        } else {
            iSiteService.update(site);
        }
        return ResponseData.ok();
    }

    @PostMapping("/checkSiteName")
    @ApiOperation(value = "站点重名校验", notes = "时间：2020/03/22")
    public ResponseData checkSiteName(@RequestBody @Validated IdNameModel<Long> checkName) throws Exception {
        iSiteService.checkName(checkName);
        return ResponseData.ok();
    }

    @GetMapping("/modelSite/{id}")
    @ApiOperation(value = "查询单个站点", notes = "时间：2020/03/22")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "站点id", dataType = "Long", required = true))
    public ResponseData<Site> modelSite(@PathVariable Long id) throws Exception {
        return ResponseData.ok(iSiteService.model(id));
    }

    @PostMapping("/querySiteList")
    @ApiOperation(value = "查询站点列表", notes = "时间：2020/03/22")
    public ResponseData<PageList<SiteListReturnVO>> querySiteList(@RequestBody @Validated SiteSearchVO siteSearchVO) throws Exception {
        return ResponseData.ok(iSiteService.queryList(siteSearchVO));
    }

    @GetMapping("/optionSite")
    @ApiOperation(value = "站点下拉", notes = "时间：2020/03/22")
    public ResponseData<List<IdNameModel<Long>>> optionSite() throws Exception {
        return ResponseData.ok(iSiteService.option());
    }

    @GetMapping("/deleteSite/{id}")
    @ApiOperation(value = "删除站点", notes = "时间：2020/03/22")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "站点id,多个用逗号隔开", dataType = "String", required = true))
    public ResponseData deleteSite(@PathVariable String id) throws Exception {
        iSiteService.delete(id);
        return ResponseData.ok();
    }
}
