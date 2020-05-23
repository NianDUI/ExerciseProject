package top.niandui.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Title: BaseConfig.java
 * @description: 基本配置
 * @time: 2020/4/7 14:07
 * @author: liyongda
 * @version: 1.0
 */
@EnableAsync
@Configuration
@EnableScheduling
@MapperScan("top.niandui.dao")
public class BaseConfig {
}
