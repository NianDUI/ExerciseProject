package top.niandui.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.niandui.common.base.BaseModel;

import java.util.List;

/**
 * 返回分页数据公共类
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/3/22 18:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "返回分页数据公共类")
public class PageList<T> extends BaseModel {
    @Schema(description = "总条数")
    private Long total;
    @Schema(description = "当前页数")
    private Integer pageNum;
    @Schema(description = "页大小")
    private Integer pageSize;
    @Schema(description = "开始行", hidden = true)
    private Integer startRow;
    @Schema(description = "结束行", hidden = true)
    private Integer endRow;
    @Schema(description = "当前页条数", hidden = true)
    private Integer size;
    @Schema(description = "总页数")
    private Integer pages;
    @Schema(description = "之前页数", hidden = true)
    private Integer prePage;
    @Schema(description = "之后页数", hidden = true)
    private Integer nextPage;
    @Schema(description = "是否为首页", hidden = true)
    private Boolean isFirstPage;
    @Schema(description = "是否为末页", hidden = true)
    private Boolean isLastPage;
    @Schema(description = "是否有上一页", hidden = true)
    private Boolean hasPreviousPage;
    @Schema(description = "是否有下一页", hidden = true)
    private Boolean hasNextPage;
    @Schema(description = "导航页码数", hidden = true)
    private Integer navigatePages;
    @Schema(description = "导航所有页号", hidden = true)
    private Integer[] navigatepageNums;
    @Schema(description = "导航首页页号", hidden = true)
    private Integer navigateFirstPage;
    @Schema(description = "导航末页页号", hidden = true)
    private Integer navigateLastPage;
    @Schema(description = "结果集")
    private List<T> list;
}
