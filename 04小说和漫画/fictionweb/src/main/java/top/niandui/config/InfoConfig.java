package top.niandui.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;

/**
 * @Title: InfoConfig.java
 * @description: 信息配置类
 * @time: 2020/7/3 14:12
 * @author: liyongda
 * @version: 1.0
 */
@Data
@Configuration
@ConfigurationProperties("config-info")
public class InfoConfig {
    private String contextPath = null;
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

    @Autowired
    public InfoConfig(ServletContext servletContext) {
        String contextPath = servletContext.getContextPath();
        if (contextPath != null && contextPath.trim().length() != 0) {
            this.contextPath = contextPath;
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
        this.jqueryBasePath = addContextPath(webjars + "/layui/" + layuiVersion);
    }

    public String addContextPath(String path) {
        return contextPath != null ? contextPath + path : path;
    }
}
