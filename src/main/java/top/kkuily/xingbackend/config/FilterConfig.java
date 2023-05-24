package top.kkuily.xingbackend.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import static top.kkuily.xingbackend.constant.admin.Login.ADMIN_TOKEN_KEY_IN_HEADER;

/**
 * @author 小K
 */
@Configuration
public class FilterConfig implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 设置响应头
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Expose-Headers", ADMIN_TOKEN_KEY_IN_HEADER);

        // 继续执行请求
        chain.doFilter(request, response);
    }
}
