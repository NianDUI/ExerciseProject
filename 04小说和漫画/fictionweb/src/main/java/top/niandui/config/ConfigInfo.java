package top.niandui.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;

/**
 * @Title: ConfigInfo.java
 * @description: 信息配置类
 * @time: 2020/7/3 14:12
 * @author: liyongda
 * @version: 1.0
 */
@Data
@Configuration
@ConfigurationProperties("config-info")
public class ConfigInfo {
    public static final boolean IS_WIN = System.getProperty("os.name").contains("Windows");
    private String contextPath;
    private String webjars = "/webjars";
    // layui
    private String layuiVersion;
    private String layuiBasePath;
    private String layuiCss;
    private String layuiJs;
    // jquery
    private String jqueryVersion;
    private String jqueryBasePath;
    private String jqueryJs;
    private String jqueryMinJs;
    // 文件路径
    private String filePath;
    private String winFilePath;
    private String linuxFilePath;

    @Autowired
    public ConfigInfo(ServletContext servletContext) {
        String contextPath = servletContext.getContextPath();
        if (contextPath != null && contextPath.trim().length() != 0) {
            this.contextPath = contextPath;
        } else {
            this.contextPath = null;
        }
    }

    public void setLayuiVersion(String layuiVersion) {
        this.layuiVersion = layuiVersion;
        this.layuiBasePath = addContextPath(webjars + "/layui/" + layuiVersion);
        this.layuiCss = this.layuiBasePath + "/css/layui.css";
        this.layuiJs = this.layuiBasePath + "/layui.all.js";
    }

    public void setJqueryVersion(String jqueryVersion) {
        this.jqueryVersion = jqueryVersion;
        this.jqueryBasePath = addContextPath(webjars + "/jquery/" + jqueryVersion);
        this.jqueryJs = this.jqueryBasePath + "/jquery.js";
        this.jqueryMinJs = this.jqueryBasePath + "/jquery.min.js";
    }

    public String addContextPath(String path) {
        return contextPath != null ? contextPath + path : path;
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
