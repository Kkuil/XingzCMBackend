package top.kkuily.xingbackend.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


/**
 * @author 小K
 */
@Configuration
public class FilterConfig implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 继续执行请求
        chain.doFilter(request, response);
    }
}
