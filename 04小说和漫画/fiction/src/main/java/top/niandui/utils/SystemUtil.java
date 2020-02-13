package top.niandui.utils;

/**
 * @Title: SystemUtil.java
 * @description: 系统工具
 * @time: 2020/2/13 10:54
 * @author: liyongda
 * @version: 1.0
 */
public class SystemUtil {
    public static void exit(int status){
        while (!PrintUtil.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        Runtime.getRuntime().exit(status);
    }
}
