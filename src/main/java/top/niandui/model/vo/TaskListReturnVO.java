package top.niandui.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.niandui.model.Task;

/**
 * 任务列表查询返回vo
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/1 11:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "任务列表查询返回vo")
public class TaskListReturnVO extends Task {
}
