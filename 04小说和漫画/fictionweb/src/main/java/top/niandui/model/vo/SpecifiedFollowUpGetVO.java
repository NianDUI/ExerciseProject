package top.niandui.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.niandui.common.base.BaseModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Title: SpecifiedFollowUpGetVO.java
 * @description: 获取指定章节和后续章节vo
 * @time: 2020/5/23 14:33
 * @author: liyongda
 * @version: 1.0
 */
@Data
@ApiModel(description = "获取指定章节和后续章节vo")
public class SpecifiedFollowUpGetVO extends BaseModel {
    @NotNull(message = "书籍id不能为空")
    @ApiModelProperty(value = "书籍id")
    private Long bookid;
    @NotBlank(message = "章节链接不能为空")
    @ApiModelProperty(value = "章节链接", required = true)
    private String url;
}
