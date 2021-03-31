package top.niandui.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Title: ConfigInfo.java
 * @description: 信息配置类
 * @time: 2020/7/3 14:12
 * @author: liyongda
 * @version: 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "config-info")
public class ConfigInfo {
    public static final boolean IS_WIN = System.getProperty("os.name").contains("Windows");
//    private String contextPath;
    private String webjars = "/webjars";
    // layui
    private String layuiBasePath = addContextPath(webjars + "/layui");
    private String layuiCss = layuiBasePath + "/css/layui.css";
    private String layuiJs = layuiBasePath + "/layui.all.js";
    // jquery
    private String jqueryBasePath = addContextPath(webjars + "/jquery");
    private String jqueryJs = jqueryBasePath + "/jquery.js";
    private String jqueryMinJs = jqueryBasePath + "/jquery.min.js";
    // 文件路径
    private String filePath;
    private String winFilePath;
    private String linuxFilePath;
    // token
    private String token;

//    @Autowired
//    public ConfigInfo(ServletContext servletContext) {
//        String contextPath = servletContext.getContextPath();
//        if (contextPath != null && contextPath.trim().length() != 0) {
//            this.contextPath = contextPath;
//        } else {
//            this.contextPath = null;
//        }
//    }

    public String addContextPath(String path) {
//        return contextPath != null ? contextPath + path : path;
        return path;
    }

    public void setWinFilePath(String winFilePath) {
        this.winFilePath = winFilePath;
        if (IS_WIN) {
            filePath = winFilePath;
        }
    }

    public void setLinuxFilePath(String linuxFilePath) {
        this.linuxFilePath = linuxFilePath;
        if (!IS_WIN) {
            filePath = linuxFilePath;
        }
    }
}
