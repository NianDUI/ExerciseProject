package top.niandui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.niandui.feign.HelloRemote;

/**
 * @Title: ConsumerController.java
 * @description: ConsumerController
 * @time: 2020/4/23 16:43
 * @author: liyongda
 * @version: 1.0
 */
@RestController
public class ConsumerController {
    @Autowired
    HelloRemote helloRemote;

    @RequestMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return helloRemote.hello(name);
    }
}
