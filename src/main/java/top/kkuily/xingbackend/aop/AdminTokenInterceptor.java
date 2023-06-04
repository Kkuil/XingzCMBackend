package top.kkuily.xingbackend.aop;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.kkuily.xingbackend.model.po.Admin;
import top.kkuily.xingbackend.service.impl.AdminServiceImpl;
import top.kkuily.xingbackend.utils.Token;

import static top.kkuily.xingbackend.constant.admin.Auth.*;
import static top.kkuily.xingbackend.constant.admin.Auth.ADMIN_TOKEN_KEY_IN_HEADER;

/**
 * @author 小K
 * @description 管理员拦截器
 */
@Configuration
@Aspect
@Slf4j
public class AdminTokenInterceptor {

    @Resource
    private AdminServiceImpl adminService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Around("@annotation(top.kkuily.xingbackend.anotation.AdminAuthToken)")
    public Object adminInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        // 获取请求对象
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        // 拦截处理逻辑
        String tokenInHeader = request.getHeader(ADMIN_TOKEN_KEY_IN_HEADER);
        log.info("token-interceptor: {}", tokenInHeader);
        // 1. 验证token是否为空
        if (tokenInHeader == null) {
            throw new SecurityException("Access denied");
        }

        // 2. 验证token的有效性
        // 2.1 验证token未过期并是有效的
        Claims payload = null;
        try {
            payload = Token.parse(tokenInHeader, ADMIN_TOKEN_SECRET);
        } catch (Exception e) {
            throw new SecurityException("Access denied");
        }
        if (payload == null) {
            throw new SecurityException("Access denied");
        }

        // 2.1 验证 token 的版本号
        String adminId = payload.get("id").toString();
        String tokenVersion = payload.get("version").toString();
        String tokenKey = ADMIN_TOKEN_VERSION_KEY + adminId;
        String tokenVersionInCache = stringRedisTemplate.opsForValue().get(tokenKey);
        if (!tokenVersion.equals(tokenVersionInCache)) {
            throw new SecurityException("Access denied");
        }

        // 2.2 验证token中的用户是真实用户
        String id = payload.get("id").toString();
        Admin adminInfo = adminService.getById(id);
        if (adminInfo == null) {
            throw new SecurityException("Access denied");
        }

        // 3. 刷新token
        String token = adminService.saveTokenVersion(adminInfo, false, payload);
        assert response != null;
        response.setHeader(ADMIN_TOKEN_KEY_IN_HEADER, token);
        return joinPoint.proceed();
    }
}
