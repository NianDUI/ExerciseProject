package top.niandui.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
@ApiModel(description = "文件")
public class Papers extends BaseModel {

    @ApiModelProperty(value = "文件")
    private File file;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "路径")
    private String path;
    @ApiModelProperty(value = "大小")
    private Double size;
    @ApiModelProperty(value = "大小单位")
    private String unit;
    @ApiModelProperty(value = "最后修改时间")
    private String lastModified;
    @ApiModelProperty(value = "是否存在")
    private Boolean isExists;
    @ApiModelProperty(value = "是否为文件夹")
    private Boolean isDir;
    @ApiModelProperty(value = "是否为文件")
    private Boolean isFile;

}
