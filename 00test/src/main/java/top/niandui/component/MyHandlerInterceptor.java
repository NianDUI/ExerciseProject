package top.niandui.component;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Spring自定义过滤器：
 *      实现：HandlerInterceptor 接口
 * Web 组件
 *
 * //@Component 直接放到容器中该组件不起作用，
 *      需要在 一个WebMvcConfigurer 实现类中的 addArgumentResolvers 中添加 该解析器。
 */
@Component
public class MyHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("自定义过滤器：前置 preHandle");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("自定义过滤器：后置 postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("自定义过滤器：完成后 afterCompletion");
    }
}
