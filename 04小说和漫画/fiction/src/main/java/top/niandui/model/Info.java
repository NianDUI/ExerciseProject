package utils;


import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.*;
import java.util.function.Function;

/**
 * @Title: Info.java
 * @description: Info
 * @time: 2020/1/17 11:07
 * @author: liyongda
 * @version: 1.0
 */
@ToString
public class Info {
    @NotBlank(message = "起始页URL，不能为空")
    public String startUrl;
    @NotBlank(message = "标题匹配路径，不能为空")
    public String titleXPathExpr;
    @NotEmpty(message = "标题后换行，不能为空")
    public String titleNewLine = "\r\n\r\n";
    @NotBlank(message = "内容匹配路径，不能为空")
    public String contentXPathExpr;
    @NotEmpty(message = "内容换行，不能为空")
    public String contentNewLine = "\r\n\r\n";
    @NotNull(message = "内容开始索引偏移量，不能为空")
    @Min(value = 0, message = "内容开始索引偏移量 >= 0")
    public Integer contentStartIndexOffset = 0;
    @NotNull(message = "内容结束索引偏移量，不能为空")
    @Max(value = 0, message = "内容结束索引偏移量 <= 0")
    public Integer contentEndIndexOffset = 0;
    @NotBlank(message = "跳转超链接匹配路径，不能为空")
    public String anchorXPathExpr;
    @NotNull(message = "下一页超链接索引，不能为空")
    public Integer nextAnchorIndex;
    @NotNull(message = "是否是结束路径判断，不能为空")
    public Function<String, Boolean> isEndHref = Info::isEndHrefDefaultMethod;

    /**
     * 是否是结束路径默认判断方法
     *
     * @param nextPageUrl  下一页URl
     * @return              true 是最后一页，false不是最后一页
     */
    private static Boolean isEndHrefDefaultMethod(String nextPageUrl) {
        return !nextPageUrl.toLowerCase().contains("html");
    }

}
