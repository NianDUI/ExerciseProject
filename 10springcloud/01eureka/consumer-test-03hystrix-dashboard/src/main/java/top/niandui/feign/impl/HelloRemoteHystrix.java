package top.niandui.feign.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import top.niandui.feign.HelloRemote;

/**
 * @Title: HelloRemoteHystrix.java
 * @description: HelloRemoteHystrix
 * @time: 2020/4/24 9:35
 * @author: liyongda
 * @version: 1.0
 */
@Component
public class HelloRemoteHystrix implements HelloRemote {

    @Override
    public String hello(@RequestParam(value = "name") String name) {
        return "hello" +name+", this messge send failed ";
    }
}
