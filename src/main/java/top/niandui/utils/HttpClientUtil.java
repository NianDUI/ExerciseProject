package top.niandui.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * @author 李永达
 * @version 1.0
 * @Title HttpClientUtil.java
 * @description HttpClient工具
 * @time 2021/5/19 15:59
 */
@Slf4j
public class HttpClientUtil {
    // json处理对象
    private final static ObjectMapper json = new ObjectMapper();

    static {
        // 反序列化：关闭未知属性失败
        json.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 序列化：字符串转json去除值为null的属性
        json.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

        // 设置当前包日志打印机级别
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger(HttpClientUtil.class);
        logger.setLevel(Level.DEBUG);
    }

    /**
     * 获取HttpClient构造器(支持https)
     *
     * @param uri 请求URI
     * @return HttpClient构造器
     */
    public static HttpClientBuilder getHttpClientBuilder(String uri) {
        HttpClientBuilder builder = HttpClients.custom();
        if (uri.contains("https:")) {
            try {
                SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(SSLContexts
                        .custom()
                        .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                        .build(), NoopHostnameVerifier.INSTANCE);
                builder.setSSLSocketFactory(scsf);
            } catch (Exception e) {
                log.debug("设置ssl工厂：" + e.getMessage(), e);
            }
        }
        return builder;
    }


    /**
     * GET请求
     *
     * @param uri     请求URI
     * @param headers 请求头Map
     * @return Http响应
     * @throws Exception
     */
    public static HttpResponse doGet(String uri, Map<String, String> headers) throws Exception {
        log.debug("==>  get uri: " + uri);
        CloseableHttpClient httpClient = getHttpClientBuilder(uri).build();
        HttpGet httpGet = new HttpGet(uri);
        // 设置请求头
        setHeaders(httpGet, headers);
        return httpClient.execute(httpGet);
    }

    /**
     * POST请求
     *
     * @param uri     请求URI
     * @param headers 请求头Map
     * @param body    请求体
     * @return Http响应
     * @throws Exception
     */
    public static HttpResponse doPost(String uri, Map<String, String> headers, Object body) throws Exception {
        log.debug("==> post uri: " + uri);
        CloseableHttpClient httpClient = getHttpClientBuilder(uri).build();
        HttpPost httpPost = new HttpPost(uri);
        // 标识是什么请求
        httpPost.setHeader("application-json", "true");
        // 设置请求头
        setHeaders(httpPost, headers);
        httpPost.removeHeaders("application-json");
        String bodyStr = body instanceof String ? (String) body : json.writeValueAsString(body);
        log.debug("==>  request: " + bodyStr);
        httpPost.setEntity(new StringEntity(bodyStr, StandardCharsets.UTF_8));
        return httpClient.execute(httpPost);
    }

    /**
     * PUT请求
     *
     * @param uri     请求URI
     * @param headers 请求头Map
     * @param body    请求体
     * @return Http响应
     * @throws Exception
     */
    public static HttpResponse doPut(String uri, Map<String, String> headers, Object body) throws Exception {
        log.debug("==>  put uri: " + uri);
        CloseableHttpClient httpClient = getHttpClientBuilder(uri).build();
        HttpPut httpPut = new HttpPut(uri);
        // 标识是什么请求
        httpPut.setHeader("application-json", "true");
        // 设置请求头
        setHeaders(httpPut, headers);
        httpPut.removeHeaders("application-json");
        String bodyStr = body instanceof String ? (String) body : json.writeValueAsString(body);
        log.debug("==>  request: " + bodyStr);
        httpPut.setEntity(new StringEntity(bodyStr, StandardCharsets.UTF_8));
        return httpClient.execute(httpPut);
    }

    /**
     * DELETE请求
     *
     * @param uri     请求URI
     * @param headers 请求头Map
     * @return Http响应
     * @throws Exception
     */
    public static HttpResponse doDelete(String uri, Map<String, String> headers) throws Exception {
        log.debug("==> delete uri: " + uri);
        CloseableHttpClient httpClient = getHttpClientBuilder(uri).build();
        HttpDelete httpDelete = new HttpDelete(uri);
        // 设置请求头
        setHeaders(httpDelete, headers);
        return httpClient.execute(httpDelete);
    }

    /**
     * 设置请求头
     *
     * @param httpRequest http请求对象
     * @param headers     请求头Map
     */
    public static void setHeaders(HttpRequestBase httpRequest, Map<String, String> headers) {
        if (!CollectionUtils.isEmpty(headers)) {
            // 自定义请求头不为空，设置请求头
            for (Map.Entry<String, String> header : headers.entrySet()) {
                httpRequest.setHeader(header.getKey(), header.getValue());
            }
        }
        // 判断内容类型是否存在
        if (httpRequest.containsHeader("application-json") && !httpRequest.containsHeader("Content-Type")) {
            // 不存在，设置默认内容类型
            httpRequest.setHeader("Content-Type", "application/json;charset=utf-8");
        }
    }

    /**
     * 设置基本身份验证
     *
     * @param headers  请求头Map
     * @param username 授权用户
     * @param password 授权密码
     * @return 请求头Map
     */
    public static Map<String, String> setBasicAuth(Map<String, String> headers, String username, String password) {
        // 授权信息
        byte[] authInfo = (username + ":" + password).getBytes(StandardCharsets.UTF_8);
        headers.put("Authorization", "Basic " + Base64.getEncoder().encodeToString(authInfo));
        return headers;
    }

    /**
     * 获取响应体
     *
     * @param httpResponse http响应
     * @param valueType    返回类型
     * @param <T>          返回泛型
     * @return 响应对象
     * @throws Exception
     */
    public static <T> T getResponseBody(HttpResponse httpResponse, Class<T> valueType) throws Exception {
        HttpEntity httpEntity = httpResponse.getEntity();
        if (httpEntity == null) {
            throw new RuntimeException("请求失败");
        }
        String body = EntityUtils.toString(httpEntity);
        // 打印响应日志
        printResponseLog(httpResponse, body);
        if (valueType == null || valueType == String.class) {
            // 如果要直接返回字符串，将响应体直接返回
            return (T) body;
        }
        return json.readValue(body, valueType);
    }

    // 获取响应体
    public static <T> T getResponseBody(HttpResponse httpResponse, TypeReference<T> valueTypeReference) throws Exception {
        HttpEntity httpEntity = httpResponse.getEntity();
        if (httpEntity == null) {
            throw new RuntimeException("请求失败");
        }
        String body = EntityUtils.toString(httpEntity);
        // 打印响应日志
        printResponseLog(httpResponse, body);
        if (valueTypeReference == null || valueTypeReference.getType() == String.class) {
            // 如果要直接返回字符串，将响应体直接返回
            return (T) body;
        }
        return json.readValue(body, valueTypeReference);
    }

    // 打印响应日志
    public static void printResponseLog(HttpResponse httpResponse, String body) {
        log.debug("<== response: (" + httpResponse.getStatusLine().getStatusCode() + ")" + body);
    }

    /*******************再一次封装的*******************/

    /**
     * GET请求
     *
     * @param uri       请求URI
     * @param headers   请求头Map
     * @param valueType 返回类型
     * @param <T>       返回泛型
     * @return Http响应
     * @throws Exception
     */
    public static <T> T doGet(String uri, Map<String, String> headers, Class<T> valueType) throws Exception {
        return getResponseBody(doGet(uri, headers), valueType);
    }

    // GET请求
    public static <T> T doGet(String uri, Map<String, String> headers, TypeReference<T> valueTypeReference) throws Exception {
        return getResponseBody(doGet(uri, headers), valueTypeReference);
    }

    /**
     * POST请求
     *
     * @param uri       请求URI
     * @param headers   请求头Map
     * @param body      请求体
     * @param valueType 返回类型
     * @param <T>       返回泛型
     * @return Http响应
     * @throws Exception
     */
    public static <T> T doPost(String uri, Map<String, String> headers, Object body, Class<T> valueType) throws Exception {
        return getResponseBody(doPost(uri, headers, body), valueType);
    }

    // POST请求
    public static <T> T doPost(String uri, Map<String, String> headers, Object body, TypeReference<T> valueTypeReference) throws Exception {
        return getResponseBody(doPost(uri, headers, body), valueTypeReference);
    }

    /**
     * PUT请求
     *
     * @param uri       请求URI
     * @param headers   请求头Map
     * @param body      请求体
     * @param valueType 返回类型
     * @param <T>       返回泛型
     * @return Http响应
     * @throws Exception
     */
    public static <T> T doPut(String uri, Map<String, String> headers, Object body, Class<T> valueType) throws Exception {
        return getResponseBody(doPut(uri, headers, body), valueType);
    }

    // PUT请求
    public static <T> T doPut(String uri, Map<String, String> headers, Object body, TypeReference<T> valueTypeReference) throws Exception {
        return getResponseBody(doPut(uri, headers, body), valueTypeReference);
    }

    /**
     * DELETE请求
     *
     * @param uri       请求URI
     * @param headers   请求头Map
     * @param valueType 返回类型
     * @param <T>       返回泛型
     * @return Http响应
     * @throws Exception
     */
    public static <T> T doDelete(String uri, Map<String, String> headers, Class<T> valueType) throws Exception {
        return getResponseBody(doDelete(uri, headers), valueType);
    }

    // DELETE请求
    public static <T> T doDelete(String uri, Map<String, String> headers, TypeReference<T> valueTypeReference) throws Exception {
        return getResponseBody(doDelete(uri, headers), valueTypeReference);
    }
}
