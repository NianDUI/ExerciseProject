package top.niandui.soap.service;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * @Title: Main.java
 * @description: TODO
 * @time: 2020/2/3 11:28
 * @author: liyongda
 * @version: 1.0
 */
public class ServiceMain {
    public static void main(String[] args) {
        // webservice发布地址
        String address = "http://localhost:8282/side_service/webservice";
        // 使用Endpoint类提供的publish方法发布WebService
        Endpoint.publish(address, new WebServiceImpl());
        System.out.println("WebService发布成功");
    }
}
