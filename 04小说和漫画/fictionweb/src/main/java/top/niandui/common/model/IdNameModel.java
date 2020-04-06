package top.niandui.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.niandui.common.base.BaseModel;

import javax.validation.constraints.NotBlank;

/**
 * @Title: IdNameModel.java
 * @description: 检查重名公共model
 * @time: 2020/3/22 17:28
 * @author: liyongda
 * @version: 1.0
 */
@Data
@ApiModel(description = "检查重名公共model")
public class IdNameModel<T> extends BaseModel {
    @ApiModelProperty(value = "id")
    private T id;
    @NotBlank(message = "名不能为空")
    @ApiModelProperty(value = "名", required = true)
    private String name;

    public IdNameModel() {
    }

    public IdNameModel(T id, String name) {
        this.id = id;
        this.name = name;
    }
}