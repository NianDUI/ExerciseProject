package top.niandui.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.niandui.common.model.PageOrder;

/**
 * @author 李永达
 * @version 1.0
 * @title BookSearchVO.java
 * @description 书籍查询vo
 * @time: 2020/3/28 14:54
 */
@Data
@ApiModel(description = "书籍查询VO")
public class BookSearchVO extends PageOrder {
    @ApiModelProperty(value = "书籍名称")
    private String name;
    @ApiModelProperty(value = "站点id")
    private Long siteid;
}