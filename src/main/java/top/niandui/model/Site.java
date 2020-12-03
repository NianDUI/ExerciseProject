package top.niandui.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.niandui.common.base.BaseModel;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

/**
 * @author 李永达
 * @version 1.0
 * @title Site.java
 * @description 站点实体类
 * @time 2020/03/22 16:18:53
 */
@Data
@ApiModel(description = "站点")
public class Site extends BaseModel {
	@ApiModelProperty(value = "站点id")
	private Long siteid;
	@NotBlank(message = "站点名称不能为空")
	@ApiModelProperty(value = "站点名称", required = true)
    private String name;
	@ApiModelProperty(value = "站点链接")
    private String url;
	@ApiModelProperty(value = "配置id")
	private Long configid;
	@ApiModelProperty(value = "创建时间")
	private Timestamp createtime;
}