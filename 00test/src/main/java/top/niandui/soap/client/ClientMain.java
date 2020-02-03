package top.niandui.soap.client;

/**
 * @Title: ClientMain.java
 * @description: ClientMain
 * @time: 2020/2/3 11:41
 * @author: liyongda
 * @version: 1.0
 */
public class ClientMain {
    public static void main(String[] args) {
        WebServiceImplService factory = new WebServiceImplService();
        WebServiceImpl impl = factory.getWebServiceImplPort();
        String result = impl.sayHello("住址");
        System.out.println(result);
    }
}
