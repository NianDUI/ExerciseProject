package top.niandui.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.niandui.common.base.BaseModel;

/**
 * 任务查询vo
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/1 11:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "任务查询vo")
public class TaskSearchVO extends BaseModel {
    @ApiModelProperty(value = "任务开关：0 关闭，1 开启")
    private Integer taskswitch;
}