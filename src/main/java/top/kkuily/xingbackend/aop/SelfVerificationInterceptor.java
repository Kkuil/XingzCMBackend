package top.kkuily.xingbackend.aop;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.AccessException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.kkuily.xingbackend.utils.Token;

import java.util.Map;

import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_KEY_IN_HEADER;
import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_SECRET;

/**
 * @author 小K
 */
@Aspect
@Slf4j
@Configuration
public class SelfVerificationInterceptor {

    @Around("@annotation(top.kkuily.xingbackend.anotation.SelfVerification)")
    public Object verify(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        // 获取请求对象
        HttpServletRequest request = attributes.getRequest();
        // 获取请求头
        String token = request.getHeader(USER_TOKEN_KEY_IN_HEADER);
        if (StringUtils.isEmpty(token)) {
            throw new RuntimeException("请先登录");
        } else {
            Claims parseToken;
            try {
                parseToken = Token.parse(token, USER_TOKEN_SECRET);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            String idInToken = (String) parseToken.get("id");
            // 获取请求参数
            Map<String, String[]> parameterMap = request.getParameterMap();
            String idInParam = parameterMap.get("id")[0];
            if (!idInParam.equals(idInToken)) {
                throw new RuntimeException("您无法修改他人的信息");
            }
        }
        return joinPoint.proceed();
    }
}
