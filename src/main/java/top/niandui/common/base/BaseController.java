package top.niandui.common.base;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller基类
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/3/22 18:08
 */
public abstract class BaseController {

    /**
     * 获取指定key的值
     * <br/>从请求头或请求参数中获取
     *
     * @param key 键
     * @return 值
     */
    public String getPara(String key) {
        // 获取请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 从请求头中获取指定key值
        String value = request.getHeader(key);
        if (!StringUtils.hasText(value)) {
            // 从请求参数中获取指定key值
            value = request.getParameter(key);
        }
        return "null".equals(value) ? null : value;
    }
}
