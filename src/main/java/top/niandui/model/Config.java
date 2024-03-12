package top.niandui.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.niandui.common.base.BaseModel;

/**
 * @Title: Config.java
 * @description: 配置实体类
 * @time: 2020/4/6 14:25
 * @author: liyongda
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "配置实体类")
public class Config extends BaseModel {
    @Schema(description = "配置id")
    private Long configid;
    @NotBlank(message = "配置名称不能为空")
    @Schema(description = "配置名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @NotBlank(message = "标题匹配路径不能为空")
    @Schema(description = "标题匹配路径", requiredMode = Schema.RequiredMode.REQUIRED)
    private String titlematch;
    @Min(value = 0, message = "标题后换行数量必须>=0")
    @NotNull(message = "标题后换行数量不能为空")
    @Schema(description = "标题后换行数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer titlelnnum;
    @NotBlank(message = "内容匹配路径不能为空")
    @Schema(description = "内容匹配路径", requiredMode = Schema.RequiredMode.REQUIRED)
    private String conmatch;
    @Min(value = 0, message = "内容后换行数量必须>=0")
    @NotNull(message = "内容后换行数量不能为空")
    @Schema(description = "内容后换行数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer conlnnum;
    @Min(value = 0, message = "内容开始索引偏移量必须>=0")
    @NotNull(message = "内容开始索引偏移量不能为空")
    @Schema(description = "内容开始索引偏移量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer startoffset;
    @Max(value = 0, message = "内容结束索引偏移量必须<=0")
    @NotNull(message = "内容结束索引偏移量不能为空")
    @Schema(description = "内容结束索引偏移量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer endoffset;
    @NotBlank(message = "跳转超链接匹配路径不能为空")
    @Schema(description = "跳转超链接匹配路径", requiredMode = Schema.RequiredMode.REQUIRED)
    private String amatch;
    @Min(value = 0, message = "下一页超链接索引必须>=0")
    @NotNull(message = "下一页超链接索引不能为空")
    @Schema(description = "下一页超链接索引", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer nexta;
}
