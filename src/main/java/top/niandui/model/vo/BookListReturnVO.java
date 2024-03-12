package top.niandui.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.niandui.model.Book;

/**
 * @Title: SiteListReturnVO.java
 * @description: 书籍列表查询返回vo
 * @time: 2020/4/6 16:02
 * @author: liyongda
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "书籍列表查询返回vo")
public class BookListReturnVO extends Book {
    @Schema(description = "配置名称")
    private String configname;
    @Schema(description = "站点名称")
    private String sitename;
    @Schema(description = "任务状态：无，重新获取，获取后续")
    private String taskstatusname;
    @Schema(description = "任务开关：关闭，开启")
    private String taskswitchname;
}
