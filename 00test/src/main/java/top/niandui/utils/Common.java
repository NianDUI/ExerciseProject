package top.niandui.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * 
 * @Title Common.java
 * @description 公共工具类
 * @time 2019年6月27日 上午11:25:41
 * @author huangwx
 * @version 1.0
 *
 */
public class Common {

	/**
	 * 
	 * @Title findUserSession
	 * @Description 获取当前登录账号对象
	 * @return FormMap<String,Object>
	 * @author huangwx
	 * @date 2019年6月27日 上午11:25:15
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> findUserSession() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String token = request.getHeader("Authorization");
		token = token.substring(7, token.length());
		Map<String, Object> map = JwtUtil.parseJWT(token, JwtUtil.BASE64SECURITYClient);
		return (Map<String, Object>) map.get("sub");
//		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
//		return (FormMap<String, Object>) request.getSession().getAttribute("userSession");
	}

	/**
	 * 将Map形式的键值对中的值转换为泛型形参给出的类中的属性值 t一般代表pojo类
	 * 
	 * @descript
	 * @param t
	 * @param params
	 * @author lanyuan
	 * @date 2015年3月29日
	 * @version 1.0
	 */
	public static <T extends Object> T flushObject(T t, Map<String, Object> params) {
		if (params == null || t == null)
			return t;

		Class<?> clazz = t.getClass();
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				Field[] fields = clazz.getDeclaredFields();

				for (int i = 0; i < fields.length; i++) {
					String name = fields[i].getName(); // 获取属性的名字
					Object value = params.get(name);
					if (value != null && !"".equals(value)) {
						// 注意下面这句，不设置true的话，不能修改private类型变量的值
						fields[i].setAccessible(true);
						fields[i].set(t, value);
					}
				}
			} catch (Exception e) {
			}

		}
		return t;
	}
    
	/**
	 * @title getIp
	 * @description 获取请求IP
	 * @param [request]
	 * @return java.lang.String
	 * @author huangwx
	 * @date 2019/11/18 9:27
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		String xip = request.getHeader("X-Real-IP");
		if (ip != null && ip.length() != 0 && !ip.equalsIgnoreCase("unKnown")) {
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = xip;
		if (ip != null && ip.length() != 0 && !ip.equalsIgnoreCase("unKnown")) {
			return ip;
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;

	}

}
