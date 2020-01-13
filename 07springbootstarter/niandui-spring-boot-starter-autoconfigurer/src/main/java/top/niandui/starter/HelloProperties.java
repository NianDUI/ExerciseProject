package top.niandui.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Title: HelloProperties.java
 * @description: HelloProperties
 * @time: 2020/1/13 9:06
 * @author: liyongda
 * @version: 1.0
 */
@ConfigurationProperties(prefix = "niandui.hello")
public class HelloProperties {
    private String prefix;
    private String suffix;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
