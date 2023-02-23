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
@Tag(name = "配置")
@RequestMapping("/api")
@ApiSupport(author = "李永达")
public class ConfigController extends BaseController {
    @Autowired
    private IConfigService iConfigService;

    @PostMapping("/saveConfig")
    @Operation(summary = "保存配置", description = "时间：2020/04/06<br>无id为新增,有id为修改")
    public ResponseData saveConfig(@RequestBody @Validated Config config) throws Exception {
        if (config.getConfigid() == null) {
            iConfigService.create(config);
        } else {
            iConfigService.update(config);
        }
        return ResponseData.ok();
    }

    @PostMapping("/checkConfigName")
    @Operation(summary = "配置重名校验", description = "时间：2020/04/06")
    public ResponseData checkConfigName(@RequestBody @Validated IdNameModel<Long> checkName) throws Exception {
        iConfigService.checkName(checkName);
        return ResponseData.ok();
    }

    @GetMapping("/modelConfig/{id}")
    @Operation(summary = "查询单个配置", description = "时间：2020/04/06")
    @Parameters(@Parameter(name = "id", description = "配置id", required = true))
    public ResponseData<Config> modelConfig(@PathVariable Long id) throws Exception {
        return ResponseData.ok(iConfigService.model(id));
    }

    @PostMapping("/queryConfigList")
    @Operation(summary = "查询配置列表", description = "时间：2020/04/06")
    public ResponseData<PageList<Config>> queryConfigList(@RequestBody @Validated ConfigSearchVO configSearchVO) throws Exception {
        return ResponseData.ok(iConfigService.queryList(configSearchVO));
    }

    @GetMapping("/optionConfig")
    @Operation(summary = "配置下拉", description = "时间：2020/04/06")
    public ResponseData<List<IdNameModel<Long>>> optionConfig() throws Exception {
        return ResponseData.ok(iConfigService.option());
    }

    @GetMapping("/deleteConfig/{id}")
    @Operation(summary = "删除配置", description = "时间：2020/04/06")
    @Parameters(@Parameter(name = "id", description = "配置id,多个用逗号隔开", required = true))
    public ResponseData deleteConfig(@PathVariable String id) throws Exception {
        iConfigService.delete(id);
        return ResponseData.ok();
    }
}
