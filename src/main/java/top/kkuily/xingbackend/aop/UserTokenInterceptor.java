package top.kkuily.xingbackend.aop;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.AccessException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.kkuily.xingbackend.model.po.User;
import top.kkuily.xingbackend.service.impl.UserServiceImpl;
import top.kkuily.xingbackend.utils.Token;

import static top.kkuily.xingbackend.constant.user.Auth.*;
import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_KEY_IN_HEADER;

/**
 * @author 小K
 * @description 用户拦截器
 */
@Configuration
@Slf4j
@Aspect
public class UserTokenInterceptor {

    @Resource
    private UserServiceImpl userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Around("@annotation(top.kkuily.xingbackend.anotation.UserAuthToken)")
    public Object userInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        // 获取请求对象
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        String tokenInHeader = request.getHeader(USER_TOKEN_KEY_IN_HEADER);
        // 1. 验证token是否为空
        if (StringUtils.isEmpty(tokenInHeader)) {
            throw new AccessException("请先登录");
        }

        // 2. 验证token的有效性
        // 2.1 验证token未过期并是有效的
        Claims payload = Token.parse(tokenInHeader, USER_TOKEN_SECRET);
        if (payload == null) {
            throw new AccessException("Access denied");
        }
        // 2.1 验证 token 的版本号
        String userId = payload.get("id").toString();
        String tokenVersion = payload.get("version").toString();
        String tokenKey = USER_TOKEN_VERSION_KEY + userId;
        String tokenVersionInCache = stringRedisTemplate.opsForValue().get(tokenKey);
        if (!tokenVersion.equals(tokenVersionInCache)) {
            throw new AccessException("令牌失效，请重新登录");
        }

        // 2.2 验证token中的用户是真实用户
        String id = payload.get("id").toString();
        User userInfo = userService.getById(id);
        if (userInfo == null) {
            throw new AccessException("Access denied");
        }

        // 3. 刷新token
        String token = userService.saveTokenVersion(userInfo, true, payload);
        assert response != null;
        response.setHeader(USER_TOKEN_KEY_IN_HEADER, token);
        return joinPoint.proceed();
    }
}
