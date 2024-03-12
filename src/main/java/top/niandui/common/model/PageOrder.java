package top.niandui.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.niandui.common.base.BaseModel;

/**
 * 分页排序公共model
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/3/22 16:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "分页排序公共model")
public class PageOrder extends BaseModel {
    @NotNull(message = "页数不能为空")
    @Schema(description = "页数", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer pageNum;
    @NotNull(message = "每页条数不能为空")
    @Schema(description = "每页条数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer pageSize;
    @Schema(description = "排序字段。与数据库一致,多个用逗号隔开")
    private String orderBy;
    @Schema(description = "正序倒序。Desc为倒序,Asc为正序")
    private String descOrAsc;
}
