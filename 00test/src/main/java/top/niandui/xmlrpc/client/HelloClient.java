package top.niandui.xmlrpc.client;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.URL;
import java.util.Arrays;

/**
 * Hello XML-RPC 客户端
 */
public class HelloClient {
    public static void main(String[] args) throws Exception {
        XmlRpcClientConfigImpl clientConfig = new XmlRpcClientConfigImpl();
        clientConfig.setServerURL(new URL("http://localhost:9091/XML_RPC_Server/service"));

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(clientConfig);

        String content = (String) client.execute("hello.sayHello", Arrays.asList("我是xxx"));
        System.out.println(content);
    }
}
