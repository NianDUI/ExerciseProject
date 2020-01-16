package top.niandui.utils;

import com.google.gson.Gson;
//import com.synqnc.exception.TokenCheckException;
import io.jsonwebtoken.*;
import net.sf.json.JSONObject;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaolei
 * @version 1.0
 * @Title JwtUtils.java
 * @description JWT工具类
 * @time 2017-5-5 下午1:14:22
 */
public class JwtUtil {
//    public static String BASE64SECURITYServer = "3253246safasdfsad12a213--=";
    public static String BASE64SECURITYClient = "32532dsfsdafasdffd12a213--=";
    public static int TTLMillis = 60000 * 60 * 10;//60分钟
    public static String AUDIENCE = "3435300";
    public static String ISSUER = "7685877800";

    /**
     * @title parseJWT
     * @description 解析jwt
     * @param [jsonWebToken, key]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author huangwx
     * @date 2019/11/18 11:37
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseJWT(String jsonWebToken, String key) throws RuntimeException {
        try {
            Claims claims = Jwts
                    .parser()
                    .setSigningKey(
                            DatatypeConverter.parseBase64Binary(key))
                    .parseClaimsJws(jsonWebToken).getBody();
            Gson gson = new Gson();
            Map<String, Object> map = new HashMap<String, Object>();
            map = gson.fromJson(claims.toString(), map.getClass());
            return map;
        } catch (Exception ex) {
            if (ex instanceof ExpiredJwtException) {
                throw new ExpiredJwtException(null, null, "Token失效");
            } else {
                throw new RuntimeException("Token不合法");
            }
        }
    }

    /**
     * @title createJWT
     * @description 生成token
     * @param [jsonObject, key]
     * @return java.lang.String
     * @author huangwx
     * @date 2019/11/18 11:37
     */
    public static String createJWT(JSONObject jsonObject, String key) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //给token中存储一下到期时间
        jsonObject.put("expireTime", (nowMillis + TTLMillis) + "t");
        // 生成签名密钥 就是一个base64加密后的字符串
        byte[] apiKeySecretBytes = DatatypeConverter
                .parseBase64Binary(key);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes,
                signatureAlgorithm.getJcaName());
        // 添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .setIssuedAt(now) // 创建时间
                .setSubject(jsonObject.toString()) // 主题 个人的一些信息
                .setIssuer(ISSUER) // 发送谁
                .setAudience(AUDIENCE) // 个人签名
                .signWith(signatureAlgorithm, signingKey); // 第三段密钥
        // 添加Token过期时间
        if (TTLMillis >= 0) {
            // 过期时间
            long expMillis = nowMillis + TTLMillis;
            Date exp = new Date(expMillis);
            // 系统时间之前的token都是不可以被承认的
            builder.setExpiration(exp).setNotBefore(now);
        }
        // 生成JWT
        return builder.compact();
    }


//	public static void main(String[] args) {
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("userId", "123");
//		jsonObject.put("userName", "赵雷");
//		jsonObject.put("ip", "127.0.0.1");
//		String token = createJWT(jsonObject, BASE64SECURITYClient);
//		System.out.println(token);
//	}

}