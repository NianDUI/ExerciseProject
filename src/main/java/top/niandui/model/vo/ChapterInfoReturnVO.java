package top.niandui.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "查询章节信息返回vo")
public class ChapterInfoReturnVO extends BaseModel {
    @Schema(description = "书籍")
    private Book book;
    @Schema(description = "章节")
    private Chapter chapter;
    @Schema(description = "下一章id")
    private Long nextid;
    @Schema(description = "下上一章id")
    private Long previd;
    @Schema(description = "段落列表")
    private List<Paragraph> paragraphList;
}
