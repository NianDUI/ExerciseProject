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
import top.niandui.model.Config;
import top.niandui.model.vo.ConfigSearchVO;
import top.niandui.service.IConfigService;

import java.util.List;

/**
 * @author 李永达
 * @version 1.0
 * @title ConfigController.java
 * @description 配置
 * @time: 2020/4/6 11:11
 */
@RestController
@Api(tags = "配置")
@RequestMapping("/api")
public class ConfigController extends BaseController {
    @Autowired
    private IConfigService iConfigService;

    @PostMapping("/saveConfig")
    @ApiOperation(value = "保存配置", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>无id为新增,有id为修改")
    public ResponseData saveConfig(@RequestBody @Validated Config config) throws Exception {
        if (config.getConfigid() == null) {
            iConfigService.create(config);
        } else {
            iConfigService.update(config);
        }
        return ResponseData.ok();
    }

    @PostMapping("/checkConfigName")
    @ApiOperation(value = "配置重名校验", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>")
    public ResponseData checkConfigName(@RequestBody @Validated IdNameModel<Long> checkName) throws Exception {
        iConfigService.checkName(checkName);
        return ResponseData.ok();
    }

    @GetMapping("/modelConfig/{id}")
    @ApiOperation(value = "查询单个配置", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "配置id", dataType = "Long", required = true))
    public ResponseData<Config> modelConfig(@PathVariable Long id) throws Exception {
        return ResponseData.ok(iConfigService.model(id));
    }

    @PostMapping("/queryConfigList")
    @ApiOperation(value = "查询配置列表", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>")
    public ResponseData<PageList<Config>> queryConfigList(@RequestBody @Validated ConfigSearchVO configSearchVO) throws Exception {
        return ResponseData.ok(iConfigService.queryList(configSearchVO));
    }

    @GetMapping("/optionConfig")
    @ApiOperation(value = "配置下拉", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>")
    public ResponseData<List<IdNameModel<Long>>> optionConfig() throws Exception {
        return ResponseData.ok(iConfigService.option());
    }

    @GetMapping("/deleteConfig/{id}")
    @ApiOperation(value = "删除配置", notes = "<br>开发人：李永达<br>时间：2020/04/06<br>")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "配置id,多个用逗号隔开", dataType = "String", required = true))
    public ResponseData deleteConfig(@PathVariable String id) throws Exception {
        iConfigService.delete(id);
        return ResponseData.ok();
    }
}
