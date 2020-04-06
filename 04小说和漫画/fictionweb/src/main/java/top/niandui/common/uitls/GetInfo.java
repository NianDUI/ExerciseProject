package top.niandui.common.uitls;

import ch.qos.logback.core.PropertyDefinerBase;
import org.springframework.boot.system.ApplicationHome;

/**
 * @Title: LogInfo.java
 * @description: 获取log信息
 * @time: 2020/3/22 17:04
 * @author: liyongda
 * @version: 1.0
 */
public class GetInfo extends PropertyDefinerBase {
    public static final String serviceName;
    public static final String serviceIp;
    public static final String logDir;
    private static int i = 0;

    static {
        serviceName ="fictionweb";
        String hostAddress;
        try {
            hostAddress = GetIp.getLocalIp4Address().get().getHostAddress();
        } catch (Exception e) {
            hostAddress = "127.0.0.1";
        }
        serviceIp = hostAddress;
        // 动态设置日志目录
        logDir =  new ApplicationHome(GetInfo.class).toString() + "/";
    }

    @Override
    public String getPropertyValue() {
        i++;
        if (i == 1) {
            return logDir;
        } else if (i == 2) {
            return serviceIp;
        } else {
            return serviceName;
        }
    }
}
