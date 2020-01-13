package top.niandui.springboot.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.niandui.starter.HelloService;

/**
 * @Title: HelloCommandLineRunner.java
 * @description: HelloCommandLineRunner
 * @time: 2020/1/13 9:24
 * @author: liyongda
 * @version: 1.0
 */
@Component
public class HelloCommandLineRunner implements CommandLineRunner {
    @Autowired
    HelloService helloService;
    @Override
    public void run(String... args) throws Exception {
        System.out.println(helloService.sayHello("HelloCommandLineRunner"));
    }
}
