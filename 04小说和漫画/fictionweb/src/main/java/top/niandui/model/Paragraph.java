package top.niandui.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.niandui.common.base.BaseModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * @Title: Paragraph.java
 * @description: 段落实体类
 * @time: 2020/3/28 14:39
 * @author: liyongda
 * @version: 1.0
 */
@Data
@ApiModel(description = "段落实体类")
public class Paragraph extends BaseModel {
    @ApiModelProperty(value = "段落id")
    private Long paragraphid;
    @ApiModelProperty(value = "创建时间")
    private Timestamp createtime;
    @NotNull(message = "排序id不能为空")
    @ApiModelProperty(value = "排序id", required = true)
    private Long seqid;
    @NotBlank(message = "段落内容不能为空")
    @ApiModelProperty(value = "段落内容", required = true)
    private String content;
    @NotNull(message = "书籍id不能为空")
    @ApiModelProperty(value = "书籍id", required = true)
    private Long bookid;
    @NotNull(message = "章节id不能为空")
    @ApiModelProperty(value = "章节id", required = true)
    private Long chapterid;
}
