package top.niandui.component;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import top.niandui.annotation.MyToken;

/**
 * springboot自定义方法参数解析器：
 *      实现：HandlerMethodArgumentResolver 接口
 * Web 组件
 *
 * //@Component 直接放到容器中该组件不起作用，
 *      需要在 一个WebMvcConfigurer 实现类中的 addArgumentResolvers 中添加 该解析器。
 */
public class MyHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    // 判断是否使用该参数解析器
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        MyToken annotation = parameter.getParameterAnnotation(MyToken.class);
        return annotation != null;
    }

    // 使用该解析器时返回什么信息
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return "这是一个测试 token";
    }

}
