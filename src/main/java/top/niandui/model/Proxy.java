package top.niandui.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.niandui.common.base.BaseModel;

/**
 * 代理
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/25 15:08
 */
@Data
@Schema(description = "代理")
public class Proxy extends BaseModel {
    @Schema(description = "代理id")
    private Integer proxyid;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "地址")
    private String host;
    @Schema(description = "端口号")
    private Integer port;
    @Schema(description = "类型：1 socks、2 http")
    private Integer type;
}
