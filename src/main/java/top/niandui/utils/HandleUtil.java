package top.niandui.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 处理工具
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/4/6 18:30
 */
@Slf4j
public class HandleUtil {

    // 休眠处理器
    public static Supplier<Long> sleepHandler = () -> {
        long sleepTime = (long) (Math.random() * 4000 + 2000);
        try {
            log.info("sleep：" + (sleepTime / 1000.0) + "s");
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        return sleepTime;
    };

    /**
     * 获取标题处理器
     *
     * @param handleInfo 处理信息
     * @return 标题处理器
     */
    public static Function<String, String> getTitleHandler(Map handleInfo) {
        try {
            Integer titleType = (Integer) handleInfo.get("titleType");
            Integer startIndex = (Integer) handleInfo.get("startIndex");
            if (titleType == 1) {
                String delimiter = (String) handleInfo.get("delimiter");
                return title -> titleHandler(title, startIndex, delimiter, delimiter.length());
            } else if (titleType == 2) {
                return title -> titleHandler(title, startIndex,
                        title.substring(startIndex).replaceAll("^\\d*", ""), 0);
            }
            return title -> title.substring(startIndex);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return title -> title;
        }
    }

    /**
     * 获取是否为最后一页判断器
     *
     * @param handleInfo 处理信息
     * @return 是否为最后一页判断器
     */
    public static BiFunction<String, String, Boolean> getIsEndHref(Map handleInfo) {
        try {
            Integer endType = (Integer) handleInfo.get("endType");
            if (endType == 1) {
                // 结束符
                String endCharacter = (String) handleInfo.get("endCharacter");
                return (thisLink, nextLink) -> nextLink.endsWith(endCharacter);
            }
            return HandleUtil::isEndHrefDefaultMethod;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return HandleUtil::isEndHrefDefaultMethod;
        }
    }

    /**
     * 标题分割处理方法1：**(开始分割索引)123(分割符)xxx -> 第123章 xxx
     *
     * @param title      标题
     * @param startIndex 开始分割索引
     * @param delimiter  分割符
     * @param delLength  分割长度
     * @return 处理后的标题
     */
    public static String titleHandler(String title, int startIndex, String delimiter, int delLength) {
        int index = title.indexOf(delimiter);
        if (index > startIndex) {
            title = "第" + title.substring(startIndex, index).trim() + "章 " + title.substring(index + delLength).trim();
        }
        return title;
    }

    /**
     * 是否是结束路径默认判断方法
     * 下一页链接不以html结束或下一页链接和本页链接末尾相同，视为最后一页t
     *
     * @param thisLink 本页URL
     * @param nextLink 下一页Href
     * @return true 是最后一页，false不是最后一页
     */
    public static Boolean isEndHrefDefaultMethod(String thisLink, String nextLink) {
        return !nextLink.toLowerCase().endsWith("html") || thisLink.endsWith(nextLink);
    }
}
