package top.niandui.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import top.niandui.common.base.BaseModel;

import java.sql.Timestamp;

/**
 * @Title: Paragraph.java
 * @description: 段落实体类
 * @time: 2020/3/28 14:39
 * @author: liyongda
 * @version: 1.0
 */
@Data
@Schema(description = "段落实体类")
public class Paragraph extends BaseModel {
    @Schema(description = "创建时间")
    private Timestamp createtime;
    @NotNull(message = "排序id不能为空")
    @Schema(description = "排序id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long seqid;
    @NotBlank(message = "段落内容不能为空")
    @Schema(description = "段落内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;
    @NotNull(message = "书籍id不能为空")
    @Schema(description = "书籍id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long bookid;
    @NotNull(message = "章节id不能为空")
    @Schema(description = "章节id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long chapterid;
}
