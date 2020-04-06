package top.niandui.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.niandui.common.base.BaseModel;

import java.util.List;

/**
 * @Title: PageList.java
 * @description: 返回分页数据公共类
 * @time: 2020/3/22 18:32
 * @author: liyongda
 * @version: 1.0
 */
@Data
@ApiModel(description = "返回分页数据公共类")
public class PageList<T> extends BaseModel {
    @ApiModelProperty(value = "总条数")
    private Long total;
    @ApiModelProperty(value = "当前页数")
    private Integer pageNum;
    @ApiModelProperty(value = "页大小")
    private Integer pageSize;
    @ApiModelProperty(value = "开始行", hidden = true)
    private Integer startRow;
    @ApiModelProperty(value = "结束行", hidden = true)
    private Integer endRow;
    @ApiModelProperty(value = "当前页条数", hidden = true)
    private Integer size;
    @ApiModelProperty(value = "总页数")
    private Integer pages;
    @ApiModelProperty(value = "之前页数", hidden = true)
    private Integer prePage;
    @ApiModelProperty(value = "之后页数", hidden = true)
    private Integer nextPage;
    @ApiModelProperty(value = "是否为首页", hidden = true)
    private Boolean isFirstPage;
    @ApiModelProperty(value = "是否为末页", hidden = true)
    private Boolean isLastPage;
    @ApiModelProperty(value = "是否有上一页", hidden = true)
    private Boolean hasPreviousPage;
    @ApiModelProperty(value = "是否有下一页", hidden = true)
    private Boolean hasNextPage;
    @ApiModelProperty(value = "导航页码数", hidden = true)
    private Integer navigatePages;
    @ApiModelProperty(value = "导航所有页号", hidden = true)
    private Integer[] navigatepageNums;
    @ApiModelProperty(value = "导航首页页号", hidden = true)
    private Integer navigateFirstPage;
    @ApiModelProperty(value = "导航末页页号", hidden = true)
    private Integer navigateLastPage;
    @ApiModelProperty(value = "结果集")
    private List<T> list;
}
