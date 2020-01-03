package top.niandui.xmlrpc.server;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.server.XmlRpcStreamServer;
import org.apache.xmlrpc.webserver.WebServer;

/**
 * Hello XML-RPC 服务端
 *      服务端，默认配置的名字是 XML_RPC_Server,   url 的地址是service
 */
public class HelloServer {
    public static void main(String[] args) throws Exception {
        WebServer webServer = new WebServer(9091);
        XmlRpcStreamServer xmlRpcServer = webServer.getXmlRpcServer();

        PropertyHandlerMapping phm = new PropertyHandlerMapping();
//        phm.load(Thread.currentThread().getContextClassLoader(), "HelloHandlers.properties");
        phm.addHandler("hello", HelloHandler.class);
        xmlRpcServer.setHandlerMapping(phm);
        XmlRpcServerConfigImpl config = new XmlRpcServerConfigImpl();
        XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();

        webServer.start();
    }
}
