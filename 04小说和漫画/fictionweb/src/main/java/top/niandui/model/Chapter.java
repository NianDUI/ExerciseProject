package top.niandui.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.niandui.common.base.BaseModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * @Title: Chapter.java
 * @description: 章节实体类
 * @time: 2020/3/28 14:33
 * @author: liyongda
 * @version: 1.0
 */
@Data
@ApiModel(description = "章节实体类")
public class Chapter extends BaseModel {
    @ApiModelProperty(value = "章节id")
    private Long chapterid;
    @NotBlank(message = "章节名称不能为空")
    @ApiModelProperty(value = "章节名称", required = true)
    private String name;
    @ApiModelProperty(value = "书籍id")
    private Long bookid;
    @ApiModelProperty(value = "配置id")
    private Long configid;
    @NotBlank(message = "原章节名称不能为空")
    @ApiModelProperty(value = "原章节名称", required = true)
    private String rawname;
    @ApiModelProperty(value = "创建时间")
    private Timestamp createtime;
    @NotNull(message = "排序id不能为空")
    @ApiModelProperty(value = "排序id", required = true)
    private Long seqid;
    @NotBlank(message = "章节链接不能为空")
    @ApiModelProperty(value = "章节链接", required = true)
    private String url;
}
