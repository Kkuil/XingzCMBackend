package top.kkuily.xingbackend.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.kkuily.xingbackend.web.interceptor.admin.AdminTokenInterceptor;
import top.kkuily.xingbackend.web.interceptor.user.UserTokenInterceptor;
import top.kkuily.xingbackend.web.interceptor.GlobalInterceptor;


/**
 * @author 小K
 * @description 拦截器配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private AdminTokenInterceptor adminTokenInterceptor;

    @Resource
    private UserTokenInterceptor userTokenInterceptor;

    @Resource
    private GlobalInterceptor globalInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 全局拦截器
        registry
                .addInterceptor(globalInterceptor)
                .addPathPatterns("/**/*")
                .order(0);
        // 管理员信息拦截器
        registry
                .addInterceptor(adminTokenInterceptor)
                .addPathPatterns(
                        "/xingz_cm/admin-*"
                )
                .excludePathPatterns(
                        "/xingz_cm/admin-login-account",
                        "/xingz_cm/admin-login-phone"
                )
                .order(1);
        // 用户信息拦截器
        registry
                .addInterceptor(userTokenInterceptor)
                .addPathPatterns(
                        "/xingz_cm/user-*"
                )
                .excludePathPatterns(
                        "/xingz_cm/user-login-account",
                        "/xingz_cm/user-login-phone",
                        "/xingz_cm/user-registry",
                        "/xingz_cm/user-forget-pwd"
                )
                .order(1);
    }
}
