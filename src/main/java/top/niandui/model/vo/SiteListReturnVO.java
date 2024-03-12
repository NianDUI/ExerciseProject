package top.niandui.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.niandui.model.Site;

/**
 * @Title: SiteListReturnVO.java
 * @description: 站点列表查询返回vo
 * @time: 2020/4/6 16:02
 * @author: liyongda
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "站点列表查询返回vo")
public class SiteListReturnVO extends Site {
    @Schema(description = "配置名称")
    private String configname;
}
