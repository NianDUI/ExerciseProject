package top.niandui.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import top.niandui.common.base.BaseModel;

/**
 * @Title: SpecifiedFollowUpGetVO.java
 * @description: 获取指定章节和后续章节vo
 * @time: 2020/5/23 14:33
 * @author: liyongda
 * @version: 1.0
 */
@Data
@Schema(description = "获取指定章节和后续章节vo")
public class SpecifiedFollowUpGetVO extends BaseModel {
    @NotNull(message = "书籍id不能为空")
    @Schema(description = "书籍id")
    private Long bookid;
    @NotBlank(message = "章节链接不能为空")
    @Schema(description = "章节链接", requiredMode = Schema.RequiredMode.REQUIRED)
    private String url;
}
