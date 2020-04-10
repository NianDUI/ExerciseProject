package top.niandui.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.niandui.common.base.BaseModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

/**
 * @Title: Book.java
 * @description: 书籍实体类
 * @time: 2020/3/28 14:28
 * @author: liyongda
 * @version: 1.0
 */
@Data
@ApiModel(description = "书籍实体类")
public class Book extends BaseModel {
    @ApiModelProperty(value = "书籍id")
    private Long bookid;
    @NotBlank(message = "书籍名称不能为空")
    @ApiModelProperty(value = "书籍名称", required = true)
    private String name;
    @ApiModelProperty(value = "书籍链接")
    private String url;
    @NotNull(message = "配置id不能为空")
    @ApiModelProperty(value = "配置id", required = true)
    private Long configid;
    @ApiModelProperty(value = "创建时间")
    private Timestamp createtime;
    @Pattern(regexp = "https?://[-A-Za-z0-9+@#/%=~_|.:\\u4e00-\\u9fa5]+(\\?[-A-Za-z0-9+&@#/%=~_|\\u4e00-\\u9fa5]*)?", message = "起始地址错误")
    @NotBlank(message = "起始章节链接不能为空")
    @ApiModelProperty(value = "起始章节链接", required = true)
    private String starturl;
    @NotBlank(message = "处理信息不能为空")
    @ApiModelProperty(value = "处理信息", required = true)
    private String handlerinfo;
    @NotNull(message = "站点id不能为空")
    @ApiModelProperty(value = "站点id", required = true)
    private Long siteid;
    @ApiModelProperty(value = "任务状态：0无，1重新获取全部，2获取后续章节，3重新获取单章")
    private Integer taskstatus;
}
