package top.niandui.common.base;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * Controller基类
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/3/22 18:08
 */
public abstract class BaseController {

    /**
     * 线程中获取请求对象
     *
     * @return 请求对象
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 线程中获取响应对象
     *
     * @return 响应
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 获取指定key的值
     * <br/>从请求头或请求参数中获取
     *
     * @param key 键
     * @return 值
     */
    public static String getPara(String key) {
        // 获取请求对象
        HttpServletRequest request = getRequest();
        // 从请求头中获取指定key值
        String value = request.getHeader(key);
        if (!StringUtils.hasText(value)) {
            // 从请求参数中获取指定key值
            value = request.getParameter(key);
        }
        if (!StringUtils.hasText(value)) {
            // 从cookie中获取
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (Objects.equals(key, cookie.getName())) {
                        value = cookie.getValue();
                        break;
                    }
                }
            }
        }
        return "null".equals(value) ? null : value;
    }
}
