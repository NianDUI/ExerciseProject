package top.niandui.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.niandui.common.model.PageOrder;

import javax.validation.constraints.NotNull;

/**
 * @author 李永达
 * @version 1.0
 * @title ParagraphSearchVO.java
 * @description 段落查询VO
 * @time: 2020/3/28 14:54
 */
@Data
@ApiModel(description = "段落查询VO")
public class ParagraphSearchVO extends PageOrder {
    @ApiModelProperty(value = "书籍id")
    private Long bookid;
    @ApiModelProperty(value = "章节id")
    private Long chapterid;
}