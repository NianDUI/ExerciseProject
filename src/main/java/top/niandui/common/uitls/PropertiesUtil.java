package top.niandui.common.uitls;

import org.apache.ibatis.io.Resources;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.io.Reader;
import java.util.Properties;

/**
 * 解析配置文件
 *
 * @author huangwx
 * @version 1.0
 * @date 2019/12/13 14:40
 */
public class PropertiesUtil {
    /**
     * 获取yaml文件的Properties对象
     *
     * @param fileName 文件名
     * @return Properties对象
     */
    public static Properties getYamlProperties(String fileName) {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource(fileName));
        return yaml.getObject();
    }

    /**
     * 获取properties文件的Properties对象
     *
     * @param fileName 文件名　(注意：加载的是根类路径下的文件,如果在某个包下．请把包名加上)
     * @return Properties对象
     */
    public static Properties getProperties(String fileName) {
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
