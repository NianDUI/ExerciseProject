package top.niandui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.niandui.interceptor.TokenInterceptor;

/**
 * @Title: WebMvcConfig.java
 * @description: WebMvc配置
 * @time: 2020/4/4 17:36
 * @author: liyongda
 * @version: 1.0
 */
@Configuration
public class WebMvcConfig {
    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Bean
    public WebMvcConfigurer myWebMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                // 添加视图控制器
                registry.addViewController("/").setViewName("main");
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                // 添加拦截器
                registry.addInterceptor(tokenInterceptor).addPathPatterns("/api/**")
                        .excludePathPatterns("/api/*download*/**", "/api/**/*download*/**", "/api/file/read");
            }
        };
    }
}
