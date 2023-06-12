package top.kkuily.xingbackend.aop;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.kkuily.xingbackend.anotation.Permission;
import top.kkuily.xingbackend.model.enums.AUTHEnums;
import top.kkuily.xingbackend.mapper.AdminMapper;
import top.kkuily.xingbackend.mapper.RoleMapper;
import top.kkuily.xingbackend.model.po.Admin;
import top.kkuily.xingbackend.model.po.Role;
import top.kkuily.xingbackend.utils.Token;

import java.lang.reflect.Method;
import java.util.Arrays;

import static top.kkuily.xingbackend.constant.admin.Auth.*;

/**
 * @author 小K
 * @description 权限校验拦截器
 */
@Aspect
@Slf4j
@Configuration
public class PermissionCheckInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private RoleMapper roleMapper;

    /**
     * @param joinPoint ProceedingJoinPoint
     * @return Object
     * @description 权限校验
     */
    @Around("@annotation(top.kkuily.xingbackend.anotation.Permission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        // 获取请求对象
        HttpServletRequest request = attributes.getRequest();
        // 获取方法体对象
        Method method = getMethod(joinPoint);
        if (method != null && !checkPermission(method, request)) {
            throw new SecurityException("Access denied");
        }
        return joinPoint.proceed();
    }

    public boolean checkPermission(Method method, HttpServletRequest request) {
        // 获取方法上的@CheckPermission注解
        Permission permission = method.getAnnotation(Permission.class);
        log.info("permission: {}", permission);
        if (permission == null) {
            // 没有指定权限，允许访问
            return true;
        } else {
            // 权限校验
            return authToken(permission.authId(), request);
        }
    }

    /**
     * @description token校验
     * @param auth AUTHEnums
     * @param request HttpServletRequest
     * @return boolean
     */
    public boolean authToken(AUTHEnums auth, HttpServletRequest request) {
        // 1. 获取token
        String token = request.getHeader(ADMIN_TOKEN_KEY_IN_HEADER);
        // 2. 判空
        if (token == null) {
            return false;
        }
        // 3. 判断有效性
        try {
            Claims payload = Token.parse(token, ADMIN_TOKEN_SECRET);
            // 3.1 获取管理员ID
            String adminId = (String) payload.get("id");
            if (adminId == null) {
                return false;
            }

            // 3.2 判断版本号是否一致
            String tokenVersion = payload.get("version").toString();
            String tokenKey = ADMIN_TOKEN_VERSION_KEY + adminId;
            String tokenVersionInCache = stringRedisTemplate.opsForValue().get(tokenKey);
            if (!tokenVersion.equals(tokenVersionInCache)) {
                return false;
            }

            // 4. 判断管理员是否存在
            Admin admin = adminMapper.selectById(adminId);
            if (admin == null) {
                return false;
            }

            // 5. 验证是否有权限访问
            String roleId = admin.getRoleId();
            Role role = roleMapper.selectById(roleId);
            String authList = role.getAuthIds();
            String[] list = authList.split(",");
            log.info("PermissionCheck: {}", true);
            log.info("list: {}", (Object) list);
            log.info("authId: {}", auth.getAuthId());
            return Arrays.stream(list).anyMatch(authId -> String.valueOf(auth.getAuthId()).equals(authId));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @description 获取方法体对象
     * @param joinPoint ProceedingJoinPoint
     * @return Method
     */
    private Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
