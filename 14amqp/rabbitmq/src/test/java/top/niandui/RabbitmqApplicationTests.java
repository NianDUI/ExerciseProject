package top.niandui;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * SpringBoot框架学习(九)——消息队列
 * https://blog.csdn.net/qq_41936805/article/details/88896623
 *
 * docker rabbitmq 3-management 5672:代码调用接口 15672:后台查看接口
 * 3-management 带web后台管理界面的
 */
@SpringBootTest
class RabbitmqApplicationTests {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void sendDirect() {
        //message要自己定义消息体内容和消息头
        //rabbitTemplate.send(exchange,routeKey,message)

        //object 默认当成消息体，只需要传入要发送的对象，自动序列化给rabbitMQ
        //rabbitTemplate.convertAndSend(exchange,routeKey,object)

        Map<String, Object> map = new HashMap<>();
        map.put("msg", "第一个消息");
        map.put("data", Arrays.asList("Hello World", 123, true));

        rabbitTemplate.convertAndSend("exchange.direct", "queues2", map);
        // 后台 消息结果显示乱码
        // 加入 bean Jackson2JsonMessageConverter 显示为json
    }

    @Test
    public void sendFanout() {
        // 发送消息给所有绑定 exchange.fanout 的队列
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "fanout消息");
        map.put("int", 123);
        rabbitTemplate.convertAndSend("exchange.fanout", "", map);
    }

    @Test
    public void receive() {
        // 未加入 Jackson2JsonMessageConverter 可已使用 该方法查看乱码消息
        // 消费一个消息
        Object receiveAndConvert = rabbitTemplate.receiveAndConvert("queues2");
        System.out.println(receiveAndConvert.getClass());
        System.out.println(receiveAndConvert);
    }

    //--------------------------使用AmqpAdmin创建exchange、queue、Binding----------------------------------

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Test
    public void amqpAdminTest() {
        // 创建Direct Exchange(交换器)
        amqpAdmin.declareExchange(new DirectExchange("java.direct"));

        // 创建 Queue
        amqpAdmin.declareQueue(new Queue("java.queue", true));

        // 将所创建的队列和交换机创建Binding
        amqpAdmin.declareBinding(new Binding("java.queue", Binding.DestinationType.QUEUE
                , "java.direct", "java.queue.routing_key", null));

    }


}
