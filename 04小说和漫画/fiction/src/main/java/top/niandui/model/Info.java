package top.niandui.model;


import lombok.ToString;

import javax.validation.constraints.*;
import java.util.Scanner;
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
    @NotNull(message = "标题后换行，不能为空")
    public String titleNewLine = "\r\n\r\n";
    @NotNull(message = "方法：标题处理，不能为空")
    public Function<String, String> titleHandler = title -> title;
    @NotBlank(message = "内容匹配路径，不能为空")
    public String contentXPathExpr;
    @NotNull(message = "内容换行，不能为空")
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
    @NotNull(message = "方法：是否是结束路径判断，不能为空")
    public Function<String, Boolean> isEndHref = Info::isEndHrefDefaultMethod;

    /**
     * 定制标题处理器
     */
    public void customizeTitleHandler() {
        Scanner sc = new Scanner(System.in);
        System.out.print("是否对标题进行处理(1处理,其他不处理)：");
        if ("1".equals(sc.nextLine().trim())) {
            System.out.println("处理示例：123(分割符)xxx -> 第123章 xxx");
            System.out.print("请输入分隔符：");
            String delimiter = sc.nextLine();
            titleHandler = title -> titleHandler(title, delimiter);
        }
    }

    /**
     * 标题处理方法1：123(分割符)xxx -> 第123章 xxx
     *
     * @param title     标题
     * @param delimiter 分割符
     * @return
     */
    private static String titleHandler(String title, String delimiter) {
        int index = title.indexOf(delimiter);
        if (index >= 0) {
            System.out.print(title + " -> ");
            title = "第" + title.substring(0, index).trim() + "章 " + title.substring(index + delimiter.length()).trim();
        }
        return title;
    }

    /**
     * 是否是结束路径默认判断方法
     *
     * @param nextPageUrl 下一页URl
     * @return true 是最后一页，false不是最后一页
     */
    private static Boolean isEndHrefDefaultMethod(String nextPageUrl) {
        return !nextPageUrl.toLowerCase().contains("html");
    }

}
