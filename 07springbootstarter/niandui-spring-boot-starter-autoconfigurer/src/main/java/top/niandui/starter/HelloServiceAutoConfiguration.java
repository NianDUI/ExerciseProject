package top.niandui.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Title: HelloAutoConfiguration.java
 * @description: HelloAutoConfiguration
 * @time: 2020/1/13 9:09
 * @author: liyongda
 * @version: 1.0
 */
@Configuration
@ConditionalOnClass(value = {HelloService.class, HelloProperties.class})
@EnableConfigurationProperties(HelloProperties.class)
public class HelloAutoConfiguration {
    @Autowired
    private HelloProperties helloProperties;

    @Bean
    public HelloService helloService() {
        HelloService service = new HelloService();
        service.helloProperties = helloProperties;
        return service;
    }
}
