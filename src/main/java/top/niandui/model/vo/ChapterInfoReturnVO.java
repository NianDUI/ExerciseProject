package top.niandui.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.niandui.common.base.BaseModel;
import top.niandui.model.Book;
import top.niandui.model.Chapter;
import top.niandui.model.Paragraph;

import java.util.List;

/**
 * @Title: ChapterInfoReturnVO.java
 * @description: 查询章节信息返回vo
 * @time: 2020/4/7 17:28
 * @author: liyongda
 * @version: 1.0
 */
@Data
@ApiModel(description = "查询章节信息返回vo")
public class ChapterInfoReturnVO extends BaseModel {
    @ApiModelProperty(value = "书籍")
    private Book book;
    @ApiModelProperty(value = "章节")
    private Chapter chapter;
    @ApiModelProperty(value = "下一章id")
    private Long nextid;
    @ApiModelProperty(value = "下上一章id")
    private Long previd;
    @ApiModelProperty(value = "段落列表")
    private List<Paragraph> paragraphList;
}
