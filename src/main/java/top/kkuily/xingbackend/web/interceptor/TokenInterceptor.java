package top.kkuily.xingbackend.web.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.kkuily.xingbackend.model.po.Admin;
import top.kkuily.xingbackend.service.impl.AdminServiceImpl;
import top.kkuily.xingbackend.utils.ErrorType;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.Token;

import static top.kkuily.xingbackend.constant.Login.*;

/**
 * @author 小K
 * @description Token 拦截器
 */
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    private AdminServiceImpl adminService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tokenInHeader = request.getHeader(TOKEN_KEY_IN_HEADER);
        // 1. 验证token是否为空
        if (tokenInHeader == null) {
            return false;
        }

        // 2. 验证token的有效性
        // 2.1 验证token未过期并是有效的
        Claims payload = Token.parse(tokenInHeader);
        if (payload == null) {
            return false;
        }

        // 2.1 验证 token 的版本号
        String adminId = payload.get("id").toString();
        String tokenVersion = payload.get("version").toString();
        String tokenKey = TOKEN_VERSION_KEY + adminId;
        String tokenVersionInCache = stringRedisTemplate.opsForValue().get(tokenKey);
        if (!tokenVersion.equals(tokenVersionInCache)) {
            return false;
        }

        // 2.2 验证token中的用户是真实用户
        String id = payload.get("id").toString();
        Admin adminInfo = adminService.getById(id);
        if (adminInfo == null) {
            return false;
        }

        // 3. 刷新token
        String token = adminService.saveTokenVersion(adminInfo, false, payload);
        response.setHeader(TOKEN_KEY_IN_HEADER, token);
        return true;
    }
}
