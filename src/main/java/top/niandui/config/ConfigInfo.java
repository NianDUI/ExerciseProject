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
// @EnableAutoConfiguration // 启用自动配置，@SpringBootApplication中包含
// 为带有@ConfigurationProperties注解的Bean提供有效的支持。
// 这个注解可以提供一种方便的方式来将带有@ConfigurationProperties注解的类注入为Spring容器的Bean。
//@EnableConfigurationProperties(ConfigInfo.class) //开启属性注入,通过@autowired注入。在使用的类上注入
@ConfigurationProperties(prefix = "config-info")
public class ConfigInfo {
    public static final boolean IS_WIN = System.getProperty("os.name").contains("Windows");
//    private String contextPath;
    private String webjars = "/webjars";
    // layui
    private String layuiBasePath = addContextPath(webjars + "/layui");
    private String layuiCss = layuiBasePath + "/css/layui.css";
    private String layuiJs = layuiBasePath + "/layui.js";
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
