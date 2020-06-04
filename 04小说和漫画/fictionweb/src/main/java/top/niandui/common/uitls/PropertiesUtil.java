package top.niandui.common.uitls;

import org.apache.ibatis.io.Resources;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.io.Reader;
import java.util.Properties;

/**
 * @author huangwx
 * @version 1.0
 * @title PropertiesUtils.java
 * @description 解析配置文件
 * @time 2019/12/13 14:40
 */
public class PropertiesUtil {
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

    /**
     * 返回　Properties
     * @param fileName 文件名　(注意：加载的是src下的文件,如果在某个包下．请把包名加上)
     * @param
     * @return
     */
    public static Properties getProperties(String fileName){
        Properties prop = new Properties();
        try {
            Reader reader = Resources.getResourceAsReader(fileName);
            prop.load(reader);
            reader.close();
        } catch (Exception e) {
            return null;
        }
        return prop;
    }
}
