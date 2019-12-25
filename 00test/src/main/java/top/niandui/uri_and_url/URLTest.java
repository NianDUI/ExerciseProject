package top.niandui.uri_and_url;

import org.junit.Test;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

/**
 * url 测试
 */
public class URLTest {
    public static void main(String[] args) throws Exception {
//        URL url = new URL("");



        URL systemResource = ClassLoader.getSystemResource("");
        System.out.println(systemResource);
        Enumeration<URL> systemResources = ClassLoader.getSystemResources("");
        while (systemResources.hasMoreElements()) {
            System.out.println(systemResources.nextElement());
        }
        System.out.println(ClassLoader.getSystemClassLoader().getResource(""));
        System.out.println(URLTest.class.getResource(""));
    }

    @Test
    public void openStream() throws Exception {
        /**
         * InputStream openStream()
         * openStream()方法连接到URL所引用的资源，在客户端和服务器之间完成必要的握手之后，返回一个InputStream实例，用于读取网络流数据。此方法返回的InputStream实例读取到的内容是Http请求体的原始报文(如果使用Http协议的话)，因此有可能是一个原始文本片段或者是一个二进制的序列(例如图片)
         */
        URL url = new URL("https://www.baidu.com");
        InputStream in = url.openStream();
        int l;
        byte[] bytes = new byte[64];
        StringBuilder sb = new StringBuilder();
        while ((l = in.read(bytes)) > 0) {
            sb.append(new String(bytes, 0, l));
        }
        System.out.println(sb);
    }

    @Test
    public void openConnection() throws Exception {
        /**
         * URLConnection openConnection()
         * openConnection()和openConnection(Proxy proxy)是相似的，只是后者可以使用代理。openConnection()方法为指定的URL新建一个socket，并且返回一个URLConnection实例，它表示一个网络资源打开的连接，我们可以从这个打开的连接获取一个InputStream实例，用于读取网络流数据。如果上面的过程出现调用失败，会抛出一个IOException。
         */
        URL url = new URL("https://www.baidu.com");
        URLConnection urlConnection = url.openConnection();
        InputStream in = urlConnection.getInputStream();
        int l;
        byte[] bytes = new byte[64];
        OutputStream ot = new FileOutputStream("bd.html");
        while ((l = in.read(bytes)) > 0) {
            ot.write(bytes, 0, l);
        }
    }
}
