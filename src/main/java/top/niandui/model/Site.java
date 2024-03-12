package top.niandui.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.niandui.common.base.BaseModel;

import java.sql.Timestamp;

/**
 * @author 李永达
 * @version 1.0
 * @title Site.java
 * @description 站点实体类
 * @time 2020/03/22 16:18:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "站点")
public class Site extends BaseModel {
	@Schema(description = "站点id")
	private Long siteid;
	@NotBlank(message = "站点名称不能为空")
	@Schema(description = "站点名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
	@Schema(description = "站点链接")
    private String url;
	@Schema(description = "配置id")
	private Long configid;
	@Schema(description = "创建时间")
	private Timestamp createtime;
}