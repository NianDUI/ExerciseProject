package top.niandui.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import top.niandui.common.expection.TokenCheckException;
import top.niandui.common.expection.TokenInvaLIDException;
import top.niandui.common.model.Authorization;
import top.niandui.common.uitls.RSAUtil;
import top.niandui.common.uitls.redis.RedisUtil;
import top.niandui.config.ConfigInfo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static top.niandui.common.base.BaseController.getPara;
import static top.niandui.common.base.BaseController.getResponse;
import static top.niandui.config.PublicConstant.DAY_SECOND;
import static top.niandui.config.PublicConstant.TOKEN;

/**
 * 授权拦截器
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/11/9 11:14
 */
@Slf4j
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    // 授权信息 key
    public final static String AUTHORIZATION_KEY = "Authorization";
    // 授权时间
    public final static long AUTHORIZATION_TIME = DAY_SECOND;
    // 存活时间
//    public final static long SURVIVAL_TIME = HOUR_SECOND + 12 * 1000;
    // token
    private static String token;
    // 是否校验token
    private static boolean isCheck;
    // json转换
    private static ObjectMapper json;
    // redis工具类
    private static RedisUtil redisUtil;

    @Autowired
    public AuthorizationInterceptor(ConfigInfo configInfo, ObjectMapper json, RedisUtil redisUtil) {
        token = configInfo.getToken();
        isCheck = StringUtils.hasText(token);
        AuthorizationInterceptor.json = json;
        AuthorizationInterceptor.redisUtil = redisUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 校验授权
        checkAuthorization();
        return true;
    }

    /**
     * 校验授权字符串
     */
    public static void checkAuthorization() {
        if (!isCheck) {
            return;
        }
        // 获取授权信息
        String authorization = getPara(AUTHORIZATION_KEY);
        try {
            String anthorizeJson = RSAUtil.privateKeyDecrypt(authorization);
            if (token.equals(anthorizeJson)) {
                return;
            }
            // 解析授权信息
            Authorization anthorize = json.readValue(anthorizeJson, Authorization.class);
            log.info(anthorize.toString());
            // 校验token
            if (!token.equals(anthorize.getToken())) {
                throw new TokenCheckException("token错误");
            }
            // 获取响应对象
            HttpServletResponse response = getResponse();
            // 过期时间
            Long expiresIn = anthorize.getExpiresIn();
            if (expiresIn == null) {
                expiresIn = System.currentTimeMillis() + AUTHORIZATION_TIME * 1000;
                // 设置过期时间
                anthorize.setExpiresIn(expiresIn);
                authorization = RSAUtil.publicKeyEncrypt(json.writeValueAsString(anthorize));
                Cookie cookie = new Cookie(AUTHORIZATION_KEY, authorization);
                // 设置cookie的最长期限（以秒为单位）
                cookie.setMaxAge(Math.toIntExact(AUTHORIZATION_TIME));
                response.addCookie(cookie);
                // 缓存token并设置过期时间
                redisUtil.set(TOKEN + authorization, expiresIn, AUTHORIZATION_TIME);
            } else {
                if (!redisUtil.hasKey(TOKEN + authorization)) {
                    // 当key不存在时：token失效
                    throw new TokenInvaLIDException("token失效");
                }
            }

            /*// 过期时间
            String key = TOKEN + authorization;
            if (redisUtil.hasKey(key)) {
                Long expiresIn = (Long) redisUtil.get(key);
                if (System.currentTimeMillis() > expiresIn) {
                    // 系统时间 超过 过期时间：token失效
                    throw new TokenInvaLIDException("token失效");
                }
            } else {
                if (anthorize.getBuildTime() < System.currentTimeMillis() - 10) {
                    // 创建时间比 10s 前更早：token错误
                    throw new TokenCheckException("token错误");
                }
                // 10s内
                long expiresIn = System.currentTimeMillis() + SURVIVAL_TIME;
                // 缓存token并设置过期时间
                redisUtil.set(key, expiresIn, DAY_SECOND);
            }*/
        } catch (JsonProcessingException e) {
            throw new RuntimeException("授权信息解析错误", e);
        }
    }

    /**
     * 校验token
     *
     * @param token token
     * @return true正确,false失败
     */
    public static boolean checkToken(String token) {
        return AuthorizationInterceptor.token.equals(RSAUtil.privateKeyDecrypt(token));
    }
}
