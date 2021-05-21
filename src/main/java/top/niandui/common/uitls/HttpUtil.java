package top.niandui.common.uitls;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Http工具
 * <br/>启用https配置参考 https://blog.csdn.net/wltsysterm/article/details/80977455
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/5/21 13:40
 */
@Slf4j
public class HttpUtil {
    private static final RestTemplate restTemplate;

    static {
        // 请求客户端工厂，支持https
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                try {
                    if (connection instanceof HttpsURLConnection) {
                        // https协议
                        HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;

                        TrustManager[] trustAllCerts = new TrustManager[]{
                                new X509TrustManager() {
                                    @Override
                                    public X509Certificate[] getAcceptedIssuers() {
                                        return null;
                                    }

                                    @Override
                                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                                    }

                                    @Override
                                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                                    }
                                }
                        };
                        SSLContext sslContext = SSLContext.getInstance("TLS");
                        sslContext.init(null, trustAllCerts, new SecureRandom());
                        httpsConnection.setSSLSocketFactory(sslContext.getSocketFactory());

                        httpsConnection.setHostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String s, SSLSession sslSession) {
                                return true;
                            }
                        });
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
                super.prepareConnection(connection, httpMethod);
            }
        };
        restTemplate = new RestTemplate(requestFactory);

        // 修改编码方式
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        StringHttpMessageConverter converter = (StringHttpMessageConverter) messageConverters.get(1);
        converter.setDefaultCharset(StandardCharsets.UTF_8);

        // 设置当前包日志打印机级别
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger(HttpUtil.class);
        logger.setLevel(Level.DEBUG);
    }

    private HttpUtil() {
    }

    /**
     * 执行请求
     *
     * @param url       请求url
     * @param method    请求方式
     * @param body      请求体
     * @param token     token
     * @param valueType 响应值类型
     * @param <P>       body参数类型
     * @param <R>       响应值类型
     * @return 响应实体
     */
    public static <P, R> ResponseEntity<R> exchange(String url, HttpMethod method, P body
            , String token, Class<R> valueType) {
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);
        // 请求实体
        HttpEntity<P> entity = new HttpEntity<>(body, headers);
        log.debug("==>  request: " + entity);
        // 执行请求
        ResponseEntity<R> exchange = restTemplate.exchange(url, method, entity, valueType);
        log.debug("<== response: " + exchange);
        return exchange;
    }

    /**
     * get请求
     *
     * @param url       请求url
     * @param token     token
     * @param valueType 响应值类型
     * @param <R>       响应值类型
     * @return 响应对象
     */
    public static <R> R get(String url, String token, Class<R> valueType) {
        log.debug("==>  get uri: " + url);
        return exchange(url, HttpMethod.GET, null, token, valueType).getBody();
    }

    /**
     * post请求
     *
     * @param url       请求url
     * @param body      请求体
     * @param token     token
     * @param valueType 响应值类型
     * @param <P>       body参数类型
     * @param <R>       响应值类型
     * @return 响应对象
     */
    public static <P, R> R post(String url, P body, String token, Class<R> valueType) {
        log.debug("==> post uri: " + url);
        return exchange(url, HttpMethod.POST, body, token, valueType).getBody();
    }

}
