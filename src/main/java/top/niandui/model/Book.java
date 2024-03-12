package top.niandui.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.niandui.common.base.BaseModel;

import java.sql.Timestamp;

/**
 * @Title: Book.java
 * @description: 书籍实体类
 * @time: 2020/3/28 14:28
 * @author: liyongda
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "书籍实体类")
public class Book extends BaseModel {
    @Schema(description = "书籍id")
    private Long bookid;
    @NotBlank(message = "书籍名称不能为空")
    @Schema(description = "书籍名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "书籍链接")
    private String url;
    @NotNull(message = "配置id不能为空")
    @Schema(description = "配置id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long configid;
    @Schema(description = "创建时间")
    private Timestamp createtime;
    @Pattern(regexp = "https?://[-A-Za-z0-9+@#/%=~_|.:\\u4e00-\\u9fa5]+(\\?[-A-Za-z0-9+&@#/%=~_|\\u4e00-\\u9fa5]*)?", message = "起始地址错误")
    @NotBlank(message = "起始章节链接不能为空")
    @Schema(description = "起始章节链接", requiredMode = Schema.RequiredMode.REQUIRED)
    private String starturl;
    @NotBlank(message = "处理信息不能为空")
    @Schema(description = "处理信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private String handlerinfo;
    @NotNull(message = "站点id不能为空")
    @Schema(description = "站点id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long siteid;
    @Schema(description = "任务状态：0无，1重新获取全部，2获取后续章节，3重新获取单章")
    private Integer taskstatus;
    @Schema(description = "任务开关：0关闭，1开启")
    private Integer taskswitch;
    @Schema(description = "书籍详情")
    private String detail;
    @Schema(description = "最后获取时间")
    private Timestamp lastgettime;
}
