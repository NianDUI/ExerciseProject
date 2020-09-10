package top.niandui.common.uitls;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Title: DateUtil.java
 * @description: 日期工具
 * @time: 2020/9/9 16:48
 * @author: liyongda
 * @version: 1.0
 */
public class DateUtil {
    // 日期格式化器：yyyy-MM-dd HH:mm:ss
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // 多线程日期格式化器：yyyy-MM-dd HH:mm:ss
    public static final ThreadLocal<SimpleDateFormat> sdfTL = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    private DateUtil() {
    }

    /**
     * 格式化日期(yyyy-MM-dd HH:mm:ss)
     *
     * @param date 时间毫秒值
     * @return 格式化后的日期
     */
    public static String dateFormat(long date) {
        return sdfTL.get().format(date);
    }

    /**
     * 格式化日期(yyyy-MM-dd HH:mm:ss)
     *
     * @param date 时间(Date)
     * @return 格式化后的日期
     */
    public static String dateFormat(Date date) {
        return sdfTL.get().format(date);
    }
}
