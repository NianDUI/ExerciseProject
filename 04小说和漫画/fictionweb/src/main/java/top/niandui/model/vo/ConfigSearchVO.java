package top.niandui.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.niandui.common.model.PageOrder;

/**
 * @author 李永达
 * @version 1.0
 * @title ConfigSearchVO.java
 * @description 配置查询VO
 * @time 2020/4/6 14:34
 */
@Data
@ApiModel(description = "配置查询VO")
public class ConfigSearchVO extends PageOrder {
    @ApiModelProperty(value = "配置名称")
    private String name;
}