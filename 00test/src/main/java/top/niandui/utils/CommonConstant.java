package top.niandui.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author huangwx
 * @version 1.0
 * @title CommonConstant.java
 * @description 公共参数
 * @time 2019/12/11 20:35
 */
@Component
public class CommonConstant {
    // 数据权限
    public static final String DATARANGE = "data_range:";

    // 专业权限
    public static final String MAJOR_RANGE = DATARANGE + "1:";

    // 用户缓存
    public static final String CACHE_USER = "cache_user:";

    // 系统版本
    public static final String OSNAME = System.getProperties().getProperty("os.name");

    // win加解密包位置
    public static String WINDESPATH;

    @Value("${WinDesPath: #{null}}")
    public void setWINDESPATH(String win) {
        WINDESPATH = win;
    }

    // Linux加解密包位置
    public static String LINUXDESPATH;

    @Value("${LinuxDesPath: #{null}}")
    public void setLINUXDESPATH(String linux) {
        LINUXDESPATH = linux;
    }
}
