package top.niandui.soap.service;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @Title: WebServiceImpl.java
 * @description: WebServiceImpl
 * @time: 2020/2/3 11:27
 * @author: liyongda
 * @version: 1.0
 */
@WebService
public class WebServiceImpl implements IWebService {

    @Override
    public String sayHello(@WebParam(name = "name") String name) {
        name = "你好：" + name;
        return name;
    }
}
