package top.niandui.config;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Title: MyBatisConfig.java
 * @description: MyBatisConfig
 * @time: 2020/1/9 11:13
 * @author: liyongda
 * @version: 1.0
 */
@Configuration
// 配置mybatis扫描路径
@MapperScan(basePackages = "top.niandui.dao")
public class MyBatisConfig {

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return new ConfigurationCustomizer() {
            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                // 下划线转为驼峰命名法开启(将映射下划线设置为大小写)
                configuration.setMapUnderscoreToCamelCase(true);
            }
        };
    }
}
