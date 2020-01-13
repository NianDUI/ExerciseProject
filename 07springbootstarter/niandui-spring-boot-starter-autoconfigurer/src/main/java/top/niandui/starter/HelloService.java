package top.niandui.starter;

/**
 * @Title: HelloService.java
 * @description: HelloService
 * @time: 2020/1/13 9:08
 * @author: liyongda
 * @version: 1.0
 */
public class HelloService {

    HelloProperties helloProperties;

    public String sayHello(String name) {
        return helloProperties.getPrefix() + "-" + name + "-" + helloProperties.getSuffix();
    }
}
