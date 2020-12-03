package top.niandui.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.niandui.common.base.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * 分页排序公共model
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/3/22 16:28
 */
@Data
@ApiModel(description = "分页排序公共model")
public class PageOrder extends BaseModel {
    @NotNull(message = "页数不能为空")
    @ApiModelProperty(value = "页数", required = true, example = "1")
    private Integer pageNum;
    @NotNull(message = "每页条数不能为空")
    @ApiModelProperty(value = "每页条数", required = true, example = "10")
    private Integer pageSize;
    @ApiModelProperty(value = "排序字段", notes = "与数据库一致,多个用逗号隔开")
    private String orderBy;
    @ApiModelProperty(value = "正序倒序", notes = "Desc为倒序,Asc为正序")
    private String descOrAsc;
}
