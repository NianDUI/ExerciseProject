package top.niandui.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import top.niandui.common.base.BaseController;
import top.niandui.common.expection.TokenCheckException;
import top.niandui.common.uitls.RSAUtil;
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
public class TokenInterceptor extends BaseController implements HandlerInterceptor {
    // token key
    public final static String TOKEN_KEY = "token";
    // token
    private static String token;
    // 是否校验token
    private static boolean isCheck;

    @Autowired
    public TokenInterceptor(ConfigInfo configInfo) {
        token = configInfo.getToken();
        isCheck = StringUtils.hasText(token);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!isCheck) {
            return true;
        }
        // 获取token
        String token = getPara(TOKEN_KEY);
        // 校验token
        if (checkToken(token)) {
            return true;
        }
        throw new TokenCheckException("token检查错误");
    }

    /**
     * 校验token
     *
     * @param token token
     * @return true正确,false失败
     */
    public static boolean checkToken(String token) {
        return TokenInterceptor.token.equals(RSAUtil.privateKeyDecrypt(token));
    }
}
