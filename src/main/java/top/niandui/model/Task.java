package top.niandui.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.niandui.common.base.BaseModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 任务实体类
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/1 11:03
 */
@Data
@ApiModel(description = "任务实体类")
public class Task extends BaseModel {
    @ApiModelProperty(value = "任务id")
    private Long taskid;
    @NotBlank(message = "任务名称不能为空")
    @ApiModelProperty(value = "任务名称", required = true)
    private String name;
    @NotBlank(message = "cron表达式不能为空")
    @ApiModelProperty(value = "cron表达式", required = true)
    private String cron;
    @NotNull(message = "任务开关不能为空")
    @ApiModelProperty(value = "任务开关：0 关闭，1 开启", required = true)
    private Integer taskswitch;
    @NotBlank(message = "任务类不能为空")
    @ApiModelProperty(value = "任务类", required = true)
    private String taskclass;
    @NotBlank(message = "任务类路径不能为空")
    @ApiModelProperty(value = "任务类路径", required = true)
    private String classpath;
    @ApiModelProperty(value = "任务描述")
    private String description;
}
