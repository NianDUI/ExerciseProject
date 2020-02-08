package top.niandui.soap.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @Title: IWebService.java
 * @description: IWebService
 * @time: 2020/2/3 11:25
 * @author: liyongda
 * @version: 1.0
 */
@WebService
public interface IWebService {

    @WebMethod
    String sayHello( String name);

}
