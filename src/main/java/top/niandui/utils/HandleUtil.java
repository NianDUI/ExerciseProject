package top.niandui.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            // 处理类型
            Integer titleType = (Integer) handleInfo.get("titleType");
            // 开始处理索引
            Integer startIndex = (Integer) handleInfo.get("startIndex");
            if (titleType == 0) {
                // 自动匹配
                // 提取第x章...，默认匹配器
                final Function<String, String>[] defaultHandler = new Function[]{null};
                // 数字开头处理器
                final Function<String, String>[] numStartHandler = new Function[]{null};
                // 包含数字匹配器
                final Function<String, String>[] includeNumHandler = new Function[]{null};
                // 使用序号
                final Function<String, String>[] useSeqNumHandler = new Function[]{null};
                handleInfo.put("startIndex", 0);
                return title -> {
                    title = title.substring(startIndex).trim();
                    if (title.matches(".*第.*章.*")) {
                        // 正常的，提取第x章...
                        if (defaultHandler[0] == null) {
                            handleInfo.put("titleType", 3);
                            defaultHandler[0] = getTitleHandler(handleInfo);
                        }
                        return defaultHandler[0].apply(title);
                    } else if (title.matches("[\\d零一二三四五六七八九十百千万亿]+")) {
                        // 全为数字
                        return "第" + title + "章";
                    } else if (title.matches("[\\d零一二三四五六七八九十百千万亿]+.*")) {
                        // 数字开头的
                        if (numStartHandler[0] == null) {
                            handleInfo.put("titleType", 2);
                            numStartHandler[0] = getTitleHandler(handleInfo);
                        }
                        return numStartHandler[0].apply(title);
                    } else if (title.matches(".*[\\d零一二三四五六七八九十百千万亿]+.*")) {
                        // 提取数字，包含数字的
                        if (includeNumHandler[0] == null) {
                            handleInfo.put("titleType", 4);
                            includeNumHandler[0] = getTitleHandler(handleInfo);
                        }
                        return includeNumHandler[0].apply(title);
                    } else if (handleInfo.get("seqNum") != null) {
                        // 使用序号
                        if (useSeqNumHandler[0] == null) {
                            handleInfo.put("titleType", 5);
                            useSeqNumHandler[0] = getTitleHandler(handleInfo);
                        }
                        return useSeqNumHandler[0].apply(title);
                    }
                    return title.trim();
                };
            } else if (titleType == 1) {
                // 分隔符拆分
                String delimiter = (String) handleInfo.get("delimiter");
                return title -> titleHandler(title, startIndex, delimiter, delimiter.length());
            } else if (titleType == 2) {
                // 数字开头拆分。将数字后面的认为分隔符，定位非章节号部分，传递过去长度0，以拆分出章节名称。
                return title -> titleHandler(title, startIndex,
                        // ^[\d零一-龥百千万亿]*
                        title.substring(startIndex).replaceAll("^[\\d零一二三四五六七八九十百千万亿]*", ""), 0);
            } else if (titleType == 3) {
                // 提取第x章...
                final Pattern pattern = Pattern.compile("第[^第章]*章.*");
                return title -> {
                    title = title.length() >= startIndex ? title.substring(startIndex) : title;
                    Matcher matcher = pattern.matcher(title);
                    // 提取到了 ? 返回提取的 ：返回原有的
                    return (matcher.find() ? matcher.group() : title).trim();
                };
            } else if (titleType == 4) {
                // 提取数字，包含数字的
                final Pattern pattern = Pattern.compile("[\\d零一二三四五六七八九十百千万亿]+");
                return title -> {
                    title = title.length() >= startIndex ? title.substring(startIndex) : title;
                    Matcher matcher = pattern.matcher(title);
                    return (matcher.find() ? "第" + matcher.group() + "章 " + title : title).trim();
                };
            } else if (titleType == 5) {
                // 使用序号
                return title -> {
                    title = title.length() >= startIndex ? title.substring(startIndex) : title;
                    Object seqNum = handleInfo.get("seqNum");
                    return (seqNum != null ? "第" + seqNum + "章 " + title : title).trim();
                };
            }
            // 无处理
            return title -> (title.length() >= startIndex ? title.substring(startIndex) : title).trim();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return String::trim;
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
        int index = title.indexOf(delimiter, startIndex);
        if (index > startIndex) {
            title = "第" + title.substring(startIndex, index).trim() + "章 " + title.substring(index + delLength).trim();
        }
        return title.trim();
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

    /**
     * 获取根uri
     *
     * @param url URL
     * @return 根uri
     */
    public static String getRootUri(String url) {
        if (url == null) {
            return url;
        }
        // https://www.147xs.org/book/150820/226230160.html
        int fromIndex = url.indexOf("://");
        if (fromIndex > -1) {
            int index = url.indexOf('/', fromIndex + 3);
            if (index > -1) {
                // 截取
                url = url.substring(0, index);
            }
        }
        return url;
    }
}
