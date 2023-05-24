package top.kkuily.xingbackend.web.interceptor.user;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.kkuily.xingbackend.model.po.User;
import top.kkuily.xingbackend.service.impl.UserServiceImpl;
import top.kkuily.xingbackend.utils.Token;

import static top.kkuily.xingbackend.constant.user.Login.*;
import static top.kkuily.xingbackend.constant.user.Login.USER_TOKEN_KEY_IN_HEADER;
import static top.kkuily.xingbackend.constant.user.Login.USER_TOKEN_SECRET;

/**
 * @author 小K
 * @description Token 拦截器
 */
@Slf4j
@Component
public class UserTokenInterceptor implements HandlerInterceptor {

    @Resource
    private UserServiceImpl userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tokenInHeader = request.getHeader(USER_TOKEN_KEY_IN_HEADER);
        System.out.println("user-token");
        // 1. 验证token是否为空
        if (tokenInHeader == null) {
            return false;
        }

        // 2. 验证token的有效性
        // 2.1 验证token未过期并是有效的
        Claims payload = Token.parse(tokenInHeader,USER_TOKEN_SECRET);
        if (payload == null) {
            return false;
        }
        // 2.1 验证 token 的版本号
        String userId = payload.get("id").toString();
        String tokenVersion = payload.get("version").toString();
        String tokenKey = USER_TOKEN_VERSION_KEY + userId;
        String tokenVersionInCache = stringRedisTemplate.opsForValue().get(tokenKey);
        if (!tokenVersion.equals(tokenVersionInCache)) {
            return false;
        }

        // 2.2 验证token中的用户是真实用户
        String id = payload.get("id").toString();
        User userInfo = userService.getById(id);
        if (userInfo == null) {
            return false;
        }

        // 3. 刷新token
        String token = userService.saveTokenVersion(userInfo, false, payload);
        response.setHeader(USER_TOKEN_KEY_IN_HEADER, token);
        return true;
    }
}
