package top.niandui.utils;

/**
 * @Title: SystemUtil.java
 * @description: 系统工具
 * @time: 2020/2/13 10:54
 * @author: liyongda
 * @version: 1.0
 */
public class SystemUtil {

    /**
     * 结束运行
     *
     * @param status 退出状态码
     */
    public static void exit(int status) {
        PrintUtil.waitPrintEnd(10);
        Runtime.getRuntime().exit(status);
    }
}
