package top.niandui.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.niandui.common.base.BaseModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Title: Config.java
 * @description: 配置实体类
 * @time: 2020/4/6 14:25
 * @author: liyongda
 * @version: 1.0
 */
@Data
@ApiModel(description = "配置实体类")
public class Config extends BaseModel {
    @ApiModelProperty(value = "配置id")
    private Long configid;
    @NotBlank(message = "配置名称不能为空")
    @ApiModelProperty(value = "配置名称", required = true)
    private String name;
    @NotBlank(message = "标题匹配路径不能为空")
    @ApiModelProperty(value = "标题匹配路径", required = true)
    private String titlematch;
    @Min(value = 0, message = "标题后换行数量必须>=0")
    @NotNull(message = "标题后换行数量不能为空")
    @ApiModelProperty(value = "标题后换行数量", required = true)
    private Integer titlelnnum;
    @NotBlank(message = "内容匹配路径不能为空")
    @ApiModelProperty(value = "内容匹配路径", required = true)
    private String conmatch;
    @Min(value = 0, message = "内容后换行数量必须>=0")
    @NotNull(message = "内容后换行数量不能为空")
    @ApiModelProperty(value = "内容后换行数量", required = true)
    private Integer conlnnum;
    @Min(value = 0, message = "内容开始索引偏移量必须>=0")
    @NotNull(message = "内容开始索引偏移量不能为空")
    @ApiModelProperty(value = "内容开始索引偏移量", required = true)
    private Integer startoffset;
    @Max(value = 0, message = "内容结束索引偏移量必须<=0")
    @NotNull(message = "内容结束索引偏移量不能为空")
    @ApiModelProperty(value = "内容结束索引偏移量", required = true)
    private Integer endoffset;
    @NotBlank(message = "跳转超链接匹配路径不能为空")
    @ApiModelProperty(value = "跳转超链接匹配路径", required = true)
    private String amatch;
    @Min(value = 0, message = "下一页超链接索引必须>=0")
    @NotNull(message = "下一页超链接索引不能为空")
    @ApiModelProperty(value = "下一页超链接索引", required = true)
    private Integer nexta;
}
