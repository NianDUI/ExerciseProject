package top.niandui.weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Title: test1.java
 * @description: 百度天气api测试
 * @time: 2020/5/25 10:54
 * @author: liyongda
 * @version: 1.0
 */
public class test1 {
    public static void main(String[] args) throws Exception{
        Map paramsMap = new LinkedHashMap<String, String>();
//        paramsMap.put("district_id", "110114");
        paramsMap.put("district_id", "110100");
        paramsMap.put("data_type", "all");
        paramsMap.put("output", "json");
        paramsMap.put("ak", "ak");

        URL url = new URL(String.format("http://api.map.baidu.com/weather/v1/?%s", toQueryString(paramsMap)));
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        System.out.println("sb = " + sb);

        Object content = urlConnection.getContent();
        System.out.println("content = " + content);
    }

    // 对Map内所有value作utf8编码，拼接返回结果
    public static String toQueryString(Map<?, ?> data)
            throws UnsupportedEncodingException {
        StringBuffer queryString = new StringBuffer();
        for (Map.Entry<?, ?> pair : data.entrySet()) {
            queryString.append(pair.getKey()).append("=");
            queryString.append(URLEncoder.encode((String) pair.getValue(), "UTF-8")).append("&");
        }
        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }
        return queryString.toString();
    }
}
