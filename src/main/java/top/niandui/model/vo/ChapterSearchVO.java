package top.niandui.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "章节查询VO")
public class ChapterSearchVO extends PageOrder {
    @Schema(description = "章节名称")
    private String name;
    @Schema(description = "书籍id")
    private Long bookid;
}