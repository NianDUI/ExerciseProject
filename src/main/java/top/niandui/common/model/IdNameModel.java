package top.niandui.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.niandui.common.base.BaseModel;


/**
 * 检查重名公共model
 *
 * @author liyongda
 * @version 1.0
 * @time 2020/3/22 17:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "检查重名公共model")
public class IdNameModel<T> extends BaseModel {
    @Schema(description = "id")
    private T id;
    @NotBlank(message = "名不能为空")
    @Schema(description = "名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    public IdNameModel() {
    }

    public IdNameModel(T id, String name) {
        this.id = id;
        this.name = name;
    }
}