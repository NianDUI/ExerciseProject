package top.niandui.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * 自定义 Filter
 */
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("自定义 Filter 执行了");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
