package top.niandui.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: HelloController.java
 * @description: HelloController
 * @time: 2020/4/23 16:25
 * @author: liyongda
 * @version: 1.0
 */
@RestController
public class HelloController {
    @Value("${endStr}")
    private String endStr;

    @RequestMapping("/hello")
    public String hello(@RequestParam String name) {
        return "hello" + name + ", this is a message。" + endStr;
    }
}
