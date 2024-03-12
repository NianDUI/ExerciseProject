package top.niandui.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.niandui.common.model.PageOrder;

/**
 * @author 李永达
 * @version 1.0
 * @title ParagraphSearchVO.java
 * @description 段落查询VO
 * @time: 2020/3/28 14:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "段落查询VO")
public class ParagraphSearchVO extends PageOrder {
    @Schema(description = "书籍id")
    private Long bookid;
    @Schema(description = "章节id")
    private Long chapterid;
}