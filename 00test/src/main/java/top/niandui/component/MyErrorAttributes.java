package top.niandui.component;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * 自定义错误属性信息
 */
@Component
public class MyErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> map = super.getErrorAttributes(webRequest, includeStackTrace);
        map.put("MyErrorAttributes", "自定义错误属性信息 MyErrorAttributes.getErrorAttributes() 传递的信息");
        map.put("MyErrorHandler", webRequest.getAttribute("MyErrorHandler", RequestAttributes.SCOPE_REQUEST));
        return map;
    }
}
