package top.niandui.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AMQPConfig
 * <p>但是现在我们想要把接收到的数据转换为json，而不是写成一个序列化的字节数组然后打印出来，所以就要自己写一个配置类去完成。</p>
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/14 10:48
 */
@Configuration
public class AMQPConfig {

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
