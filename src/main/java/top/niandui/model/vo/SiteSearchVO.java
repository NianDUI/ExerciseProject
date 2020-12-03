package top.niandui.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.niandui.common.model.PageOrder;

/**
 * @author 李永达
 * @version 1.0
 * @title SiteSearchVO.java
 * @description 站点查询vo
 * @time: 2020/3/28 14:54
 */
@Data
@ApiModel(description = "站点查询VO")
public class SiteSearchVO extends PageOrder {
    @ApiModelProperty(value = "站点名称")
    private String name;
}