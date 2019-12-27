package top.niandui.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: TestWebSocketController.java
 */
@RestController
public class TestWebSocketController {

    @MessageMapping("/websocketMapping") // @MessageMapping 和 @RequestMapping 功能类似，浏览器向服务器发起消息，映射到该地址。
    @SendTo("/msg/websocket") // 如果服务器接受到了消息，就会对订阅了 @SendTo 括号中的地址的浏览器发送消息。
    public String websocketMessage(String message) {
        return message;
    }

}
