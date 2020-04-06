package top.niandui.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Title: HandleUtils.java
 * @description: 处理工具
 * @time: 2020/4/6 18:30
 * @author: liyongda
 * @version: 1.0
 */
@Slf4j
public class HandleUtils {
    public static ObjectMapper json = new ObjectMapper();

    public static BiFunction<String, String, Boolean> isEndHref = HandleUtils::isEndHrefDefaultMethod;
    public static Supplier<Long> sleepHandler = () -> {
        long sleepTime = (long) (Math.random() * 4000 + 2000);
        try {
            log.info("sleep：" + (sleepTime / 1000.0) + "s");
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.toString());
        }
        return sleepTime;
    };

    /**
     * 获取标题处理器
     */
    public static Function<String, String> getTitleHandler(String titleHandle) {
        try {
            Map handleInfo = json.readValue(titleHandle, Map.class);
            Integer handleType = (Integer) handleInfo.get("handleType");
            Integer startIndex = (Integer) handleInfo.get("startIndex");
            if (handleType == 1) {
                String delimiter = (String) handleInfo.get("delimiter");
                return title -> titleHandler(title, startIndex, delimiter, delimiter.length());
            } else if (handleType == 2) {
                return title -> titleHandler(title, startIndex,
                        title.substring(startIndex).replaceAll("^\\d*", ""), 0);
            }
            return title -> title;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error(e.toString());
            return title -> title;
        }
    }
    /**
     *
     * 标题分割处理方法1：**(开始分割索引)123(分割符)xxx -> 第123章 xxx
     *
     * @param title      标题
     * @param startIndex 开始分割索引
     * @param delimiter  分割符
     * @param delLength  分割长度
     * @return
     */
    public static String titleHandler(String title, int startIndex, String delimiter, int delLength) {
        int index = title.indexOf(delimiter);
        if (index >= startIndex) {
            title = "第" + title.substring(startIndex, index).trim() + "章 " + title.substring(index + delLength).trim();
        }
        return title;
    }

    /**
     * 是否是结束路径默认判断方法
     *
     * @param thisLink   本页URL
     * @param nextLink   下一页Href
     * @return true 是最后一页，false不是最后一页
     */
    public static Boolean isEndHrefDefaultMethod(String thisLink, String nextLink) {
        return !nextLink.toLowerCase().contains("html") || thisLink.endsWith(nextLink);
    }
}
