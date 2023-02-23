package top.niandui.common.uitls;

import ch.qos.logback.core.PropertyDefinerBase;
import org.springframework.boot.system.ApplicationHome;

/**
 * 获取log所需信息
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/3/22 17:04
 */
public class GetInfo extends PropertyDefinerBase {
    private static final String FILE_NAME = "config/application-server.yml";
    public static final String SERVICE_NAME;
    public static final String SERVICE_IP;
    public static final String LOG_DIR;
    private static int i = 0;

    static {
        SERVICE_NAME = PropertiesUtil.getYamlProperties(FILE_NAME).getProperty("spring.application.name");
        String hostAddress;
        try {
            hostAddress = GetIp.getLocalIp4Address().get().getHostAddress();
        } catch (Exception e) {
            hostAddress = "127.0.0.1";
        }
        SERVICE_IP = hostAddress;
        // 动态设置日志目录
        // 动态设置日志目录
        ApplicationHome applicationHome = new ApplicationHome(GetInfo.class);
        String homePath = applicationHome.toString();
        if (homePath.endsWith("target\\classes")) {
            // 是开发环境，日志存放在项目目录下
            LOG_DIR = homePath + "/../../log/";
        } else {
            // 不是开发环境，存放在启动目录下
            LOG_DIR = applicationHome + "/";
        }
    }

    @Override
    public String getPropertyValue() {
        i++;
        if (i % 3 == 1) {
            return LOG_DIR;
        } else if (i % 3 == 2) {
            return SERVICE_IP;
        } else {
            return SERVICE_NAME;
        }
    }
}
