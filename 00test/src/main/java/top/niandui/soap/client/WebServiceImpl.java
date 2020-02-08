
package top.niandui.soap.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "WebServiceImpl", targetNamespace = "http://service.soap.niandui.top/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface WebServiceImpl {


    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "sayHello", targetNamespace = "http://service.soap.niandui.top/", className = "top.niandui.soap.client.SayHello")
    @ResponseWrapper(localName = "sayHelloResponse", targetNamespace = "http://service.soap.niandui.top/", className = "top.niandui.soap.client.SayHelloResponse")
    @Action(input = "http://service.soap.niandui.top/WebServiceImpl/sayHelloRequest", output = "http://service.soap.niandui.top/WebServiceImpl/sayHelloResponse")
    public String sayHello(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

}