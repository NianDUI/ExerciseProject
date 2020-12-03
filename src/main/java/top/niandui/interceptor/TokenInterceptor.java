package top.niandui.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.thymeleaf.util.StringUtils;
import top.niandui.common.expection.TokenCheckException;
import top.niandui.config.ConfigInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Token拦截器
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/11/9 11:14
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {
    // token
    private final String token;
    // 是否校验token
    private final boolean isCheck;

    @Autowired
    public TokenInterceptor(ConfigInfo configInfo) {
        token = configInfo.getToken();
        isCheck = !StringUtils.isEmpty(token);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!isCheck) {
            return true;
        }
        if (token.equals(request.getHeader("Token"))) {
            return true;
        }
        throw new TokenCheckException("token检查错误");
    }
}
