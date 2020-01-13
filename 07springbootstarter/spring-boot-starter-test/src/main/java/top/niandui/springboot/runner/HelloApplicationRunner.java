package top.niandui.springboot.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.niandui.starter.HelloService;

/**
 * @Title: HelloApplicationRunner.java
 * @description: HelloApplicationRunner
 * @time: 2020/1/13 9:22
 * @author: liyongda
 * @version: 1.0
 */
@Component
public class HelloApplicationRunner implements ApplicationRunner {
    @Autowired
    HelloService helloService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(helloService.sayHello("HelloApplicationRunner"));
    }
}
