package top.niandui.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.niandui.common.base.BaseModel;

/**
 * 任务实体类
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/1 11:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "任务实体类")
public class Task extends BaseModel {
    @Schema(description = "任务id")
    private Long taskid;
    @NotBlank(message = "任务名称不能为空")
    @Schema(description = "任务名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @NotBlank(message = "cron表达式不能为空")
    @Schema(description = "cron表达式", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cron;
    @NotNull(message = "任务开关不能为空")
    @Schema(description = "任务开关：0 关闭，1 开启", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer taskswitch;
    @NotBlank(message = "任务类路径不能为空")
    @Schema(description = "任务类路径", requiredMode = Schema.RequiredMode.REQUIRED)
    private String classpath;
    @NotNull(message = "类加载方式不能为空")
    @Schema(description = "类加载方式：0 反射、1 IOC容器", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer loadmanner;
    @Schema(description = "任务描述")
    private String description;
}
