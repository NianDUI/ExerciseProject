package top.niandui.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.niandui.model.Chapter;

/**
 * @Title: SiteListReturnVO.java
 * @description: 章节列表查询返回vo
 * @time: 2020/4/6 16:02
 * @author: liyongda
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "章节列表查询返回vo")
public class ChapterListReturnVO extends Chapter {
    @Schema(description = "配置名称")
    private String configname;
    @Schema(description = "书籍名称")
    private String bookname;
}
