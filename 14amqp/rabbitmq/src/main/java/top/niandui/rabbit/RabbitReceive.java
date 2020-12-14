package top.niandui.rabbit;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Rabbit 接受消息
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/12/14 10:59
 */
@Component
public class RabbitReceive {

    // 处理消息
    @RabbitListener(queues = "queues2")
    public void processMessage(Map map) {
        System.out.println(map.getClass());
        System.out.println("收到 queues2 消息：" + map);
    }
}
