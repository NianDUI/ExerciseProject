package top.niandui.common.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 授权实体类
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/4/12 13:46
 */
@Data
public class Authorization {
    @ApiModelProperty(value = "token")
    private String token;
    @ApiModelProperty(value = "构建")
    private Long buildTime;
    @ApiModelProperty(value = "过期日期")
    private Long expiresIn;
}
