package top.niandui.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.niandui.common.model.PageOrder;

/**
 * @author 李永达
 * @version 1.0
 * @title ChapterSearchVO.java
 * @description 章节查询VO
 * @time: 2020/3/28 14:54
 */
@Data
@ApiModel(description = "章节查询VO")
public class ChapterSearchVO extends PageOrder {
    @ApiModelProperty(value = "章节名称")
    private String name;
    @ApiModelProperty(value = "书籍id")
    private Long bookid;
}