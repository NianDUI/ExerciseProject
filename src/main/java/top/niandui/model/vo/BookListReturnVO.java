package top.niandui.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.niandui.model.Book;

/**
 * @Title: SiteListReturnVO.java
 * @description: 书籍列表查询返回vo
 * @time: 2020/4/6 16:02
 * @author: liyongda
 * @version: 1.0
 */
@Data
@ApiModel(description = "书籍列表查询返回vo")
public class BookListReturnVO extends Book {
    @ApiModelProperty(value = "配置名称")
    private String configname;
    @ApiModelProperty(value = "站点名称")
    private String sitename;
    @ApiModelProperty(value = "任务状态：无，重新获取，获取后续")
    private String taskstatusname;
    @ApiModelProperty(value = "任务开关：关闭，开启")
    private String taskswitchname;
}
