package top.kkuily.xingbackend.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.kkuily.xingbackend.web.interceptor.TokenInterceptor;


/**
 * @author 小K
 * @description 拦截器配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 排除白名单请求路径
        registry
                .addInterceptor(tokenInterceptor)
                .excludePathPatterns(
                        "/xingz_cm/admin-login"
                );
    }
}
