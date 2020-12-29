package top.niandui.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "代理")
public class Proxy extends BaseModel {
    @ApiModelProperty(value = "代理id")
    private Integer proxyid;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "地址")
    private String host;
    @ApiModelProperty(value = "端口号")
    private Integer port;
    @ApiModelProperty(value = "类型：1 socks、2 http")
    private Integer type;
}
