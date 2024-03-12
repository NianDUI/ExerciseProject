package top.niandui.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.niandui.common.base.BaseModel;

import java.io.File;

/**
 * @Title: Papers.java
 * @description: 文件
 * @time: 2020/9/9 11:33
 * @author: liyongda
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "文件")
public class Papers extends BaseModel {

    @Schema(description = "文件")
    private File file;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "路径")
    private String path;
    @Schema(description = "大小")
    private Double size;
    @Schema(description = "大小单位")
    private String unit;
    @Schema(description = "最后修改时间")
    private String lastModified;
    @Schema(description = "是否存在")
    private Boolean isExists;
    @Schema(description = "是否为文件夹")
    private Boolean isDir;
    @Schema(description = "是否为文件")
    private Boolean isFile;

}
