package top.niandui.model;


import lombok.ToString;
import top.niandui.utils.PrintUtil;

import javax.validation.constraints.*;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Title: Info.java
 * @description: Info
 * @time: 2020/1/17 11:07
 * @author: liyongda
 * @version: 1.0
 */
@ToString
public class Info {
    @Pattern(regexp = "https?://[-A-Za-z0-9+@#/%=~_|.:\\u4e00-\\u9fa5]+(\\?[-A-Za-z0-9+&@#/%=~_|\\u4e00-\\u9fa5]*)?", message = "起始地址错误")
    @NotBlank(message = "起始地址，不能为空")
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
    public BiFunction<String, StringBuilder, Boolean> isEndHref = Info::isEndHrefDefaultMethod;
    @NotBlank(message = "保存文件名称，不能为空")
    public String fileName;
    @NotNull(message = "是否保存为文件，不能为空")
    public Boolean isSaveFile = true;
    @NotNull(message = "是否追加保存，不能为空")
    public Boolean isAppendSave = false;
    @NotNull(message = "方法：休眠处理，不能为空")
    public Supplier<Long> sleepHandler = () -> {
        long sleepTime = (long) (Math.random() * 4000 + 2000);
        try {
            PrintUtil.println("sleep：" + (sleepTime / 1000.0) + "s");
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            PrintUtil.println(e);
        }
        return sleepTime;
    };

    /**
     * 定制标题处理器
     */
    public void customizeTitleHandler() {
        Scanner sc = new Scanner(System.in);
        PrintUtil.println("1: 使用分隔符。示例：123(分割符)xxx -> 第123章 xxx");
        PrintUtil.println("2: 数字开头无分隔符。示例：123xxx -> 第123章 xxx");
        PrintUtil.print("是否对标题进行处理(其他不处理)：");
        String line = sc.nextLine().trim();
        if ("1".equals(line)) {
            PrintUtil.print("请输入分隔符：");
            String delimiter = sc.nextLine();
            titleHandler = title -> titleHandler(title, delimiter, delimiter.length());
        } else if ("2".equals(line)) {
            titleHandler = title -> titleHandler(title, title.replaceAll("^\\d*", ""), 0);
        }
    }

    /**
     * 标题分割处理方法1：123(分割符)xxx -> 第123章 xxx
     *
     * @param title     标题
     * @param delimiter 分割符
     * @param delLength 分割长度
     * @return
     */
    public static String titleHandler(String title, String delimiter, int delLength) {
        int index = title.indexOf(delimiter);
        if (index >= 0) {
            System.out.print(title + " -> ");
            title = "第" + title.substring(0, index).trim() + "章 " + title.substring(index + delLength).trim();
        }
        return title;
    }

    /**
     * 是否是结束路径默认判断方法
     *
     * @param nextPageUrl 下一页URl
     * @param content     获取的本章内容
     * @return true 是最后一页，false不是最后一页
     */
    public static Boolean isEndHrefDefaultMethod(String nextPageUrl, StringBuilder content) {
        return !nextPageUrl.toLowerCase().contains("html");
    }

}
