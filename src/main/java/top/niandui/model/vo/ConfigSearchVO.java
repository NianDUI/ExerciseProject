package top.niandui.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.niandui.common.model.PageOrder;

/**
 * @author 李永达
 * @version 1.0
 * @title ConfigSearchVO.java
 * @description 配置查询VO
 * @time 2020/4/6 14:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "配置查询VO")
public class ConfigSearchVO extends PageOrder {
    @Schema(description = "配置名称")
    private String name;
}