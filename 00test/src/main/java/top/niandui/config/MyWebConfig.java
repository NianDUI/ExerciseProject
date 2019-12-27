package top.niandui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.niandui.component.MyHandlerInterceptor;
import top.niandui.component.MyHandlerMethodArgumentResolver;

import java.util.List;

/**
 * 添加自己的webmvc配置器
 */
@Configuration
public class MyWebConfig {

    @Bean
    public WebMvcConfigurer myWebMvcConfigurer() {
        return new WebMvcConfigurer() {
            // 添加自定义过滤器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new MyHandlerInterceptor())
                            .addPathPatterns("/**").excludePathPatterns("/testDate");
            }

            // 添加自定义参数解析器
            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                resolvers.add(new MyHandlerMethodArgumentResolver());
            }

            // 自定义视图控制器
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/websocket").setViewName("/websocket");
            }
        };
    }
}
