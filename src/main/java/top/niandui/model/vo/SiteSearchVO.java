package top.niandui.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.niandui.common.model.PageOrder;

/**
 * @author 李永达
 * @version 1.0
 * @title SiteSearchVO.java
 * @description 站点查询vo
 * @time: 2020/3/28 14:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "站点查询VO")
public class SiteSearchVO extends PageOrder {
    @Schema(description = "站点名称")
    private String name;
}