package top.niandui.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "书籍查询VO")
public class BookSearchVO extends PageOrder {
    @Schema(description = "书籍名称")
    private String name;
    @Schema(description = "站点id")
    private Long siteid;
}