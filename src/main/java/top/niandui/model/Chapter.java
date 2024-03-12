package top.niandui.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.niandui.common.base.BaseModel;

import java.sql.Timestamp;

/**
 * @Title: Chapter.java
 * @description: 章节实体类
 * @time: 2020/3/28 14:33
 * @author: liyongda
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "章节实体类")
public class Chapter extends BaseModel {
    @Schema(description = "章节id")
    private Long chapterid;
    @NotBlank(message = "章节名称不能为空")
    @Schema(description = "章节名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "书籍id")
    private Long bookid;
    @Schema(description = "配置id")
    private Long configid;
    @NotBlank(message = "原章节名称不能为空")
    @Schema(description = "原章节名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String rawname;
    @Schema(description = "创建时间")
    private Timestamp createtime;
    @NotNull(message = "排序id不能为空")
    @Schema(description = "排序id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long seqid;
    @NotBlank(message = "章节链接不能为空")
    @Schema(description = "章节链接", requiredMode = Schema.RequiredMode.REQUIRED)
    private String url;
}
