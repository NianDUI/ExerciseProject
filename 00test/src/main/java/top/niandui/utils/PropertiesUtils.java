package top.niandui.utils;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

/**
 * @author huangwx
 * @version 1.0
 * @title PropertiesUtils.java
 * @description 解析配置文件
 * @time 2019/12/13 14:40
 */
public class PropertiesUtils {
    /**
     * @description 解析yml
     * @author huangwx
     * @date 2019/12/13 14:25
     */
    public static String getConfiguration(String fileName, String typeName) {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource(fileName));
        Properties properties = yaml.getObject();
        return properties.getProperty(typeName);
    }
}
