package top.niandui.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.niandui.model.Chapter;

/**
 * @Title: SiteListReturnVO.java
 * @description: 章节列表查询返回vo
 * @time: 2020/4/6 16:02
 * @author: liyongda
 * @version: 1.0
 */
@Data
@ApiModel(description = "章节列表查询返回vo")
public class ChapterListReturnVO extends Chapter {
    @ApiModelProperty(value = "配置名称")
    private String configname;
    @ApiModelProperty(value = "书籍名称")
    private String bookname;
}
