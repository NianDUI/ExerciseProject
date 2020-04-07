package top.niandui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Title: WebMvcConfig.java
 * @description: WebMvc配置
 * @time: 2020/4/4 17:36
 * @author: liyongda
 * @version: 1.0
 */
@EnableAsync
@Configuration
public class WebMvcConfig {

    @Bean
    public WebMvcConfigurer myWebMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("main");
            }
        };
    }
}
