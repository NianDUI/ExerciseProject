package top.niandui.soap;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: RequestTest.java
 * @description: 不是工具生成的soap请求示例
 * @time: 2020/2/7 17:39
 * @author: liyongda
 * @version: 1.0
 */
public class RequestTest {

    public static void main(String[] args) {
        request();
        // 请求示例
        //<?xml version="1.0" ?>
        //<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
        //<S:Body>
        //<ns2:sayHello xmlns:ns2="http://service.soap.niandui.top/">
        //<arg0>住址</arg0>
        //</ns2:sayHello>
        //</S:Body></S:Envelope>

        // 响应示例
        //<?xml version="1.0" ?>
        //<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
        //<S:Body>
        //<ns2:sayHelloResponse xmlns:ns2="http://service.soap.niandui.top/">
        //<return>你好：住址</return>
        //</ns2:sayHelloResponse>
        //</S:Body>
        //</S:Envelope>
    }

    public static Map<String, String> request() {
        Map<String, String> resultMap = new HashMap<>();
        String msgContent = getMsgContent();
        String result = RequestTest.send("http://localhost:8282/side_service/webservice", msgContent);
        System.out.println(result);
        resultMap.put("result", result);
        return resultMap;
    }

    // 请求头
    private static String getHeader() {
        StringBuffer soapHeader = new StringBuffer();
        soapHeader.append("<S:Header>");
        soapHeader.append("</S:Header>");
        return soapHeader.toString();
    }

    // 请求信息
    public static String getMsgContent() {
        StringBuffer xml = new StringBuffer();
        xml.append("<?xml version=\"1.0\" ?>");
        xml.append("<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        xml.append(getHeader());
        xml.append("<S:Body>");
        xml.append("<ns2:sayHello xmlns:ns2=\"http://service.soap.niandui.top/\">");
        xml.append("<name>啊的撒发生</name>");
        xml.append("</ns2:sayHello>");
        xml.append("</S:Body>");
        xml.append("</S:Envelope>");
        return xml.toString();
    }

    /**
     * 调用接口，发送xml文档获取数据
     *
     * @param url 请求url
     * @param xml 请求内容
     * @return 响应的信息
     */
    public static String send(String url, String xml) {
        HttpURLConnection conn = null;
        InputStreamReader isr = null;
        OutputStream out = null;
        StringBuffer result = null;
        try {
            byte[] sendbyte = xml.getBytes();
            // 连接RUL
            URL connUrl = new URL(url);
            conn = (HttpURLConnection) connUrl.openConnection();
            conn.setRequestProperty("Content-Type", "text/xml");
            // 设置文件长度
            conn.setRequestProperty("Content-Length", sendbyte.length + "");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // 设置连接超时30000ms
            conn.setConnectTimeout(1000 * 30);
            // 设置读取数据超时
            conn.setReadTimeout(60000);
            out = conn.getOutputStream();
            out.write(sendbyte);
            if (conn.getResponseCode() == 200) {
                result = new StringBuffer();
                isr = new InputStreamReader(conn.getInputStream());
                char[] c = new char[1024];
                int l;
                while ((l = isr.read(c)) != -1) {
                    result.append(new String(c, 0, l));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            try {
                if (isr != null) {
                    isr.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result == null ? "" : result.toString();
    }
}
