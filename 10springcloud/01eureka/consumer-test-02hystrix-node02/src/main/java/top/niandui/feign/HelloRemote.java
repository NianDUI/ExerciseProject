package top.niandui.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.niandui.feign.impl.HelloRemoteHystrix;

/**
 * @Title: HelloRemote.java
 * @description: HelloRemote
 * @time: 2020/4/23 16:40
 * @author: liyongda
 * @version: 1.0
 */
@FeignClient(name = "spring-cloud-producer", fallback = HelloRemoteHystrix.class)
public interface HelloRemote {

    @RequestMapping("/hello")
    String hello(@RequestParam("name") String name);
}
