package top.kkuily.xingbackend.aop;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.kkuily.xingbackend.anotation.ApiSignAuth;
import top.kkuily.xingbackend.utils.ApiSignAuthUtils;

import java.lang.reflect.Method;
import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import static top.kkuily.xingbackend.constant.Auth.ApiSignKey.*;
import static top.kkuily.xingbackend.constant.commons.Auth.SIGN_KEY_IN_HEADER;

/**
 * @author 小K
 */
@Aspect
@Slf4j
@Configuration
public class ApiSignAuthInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Around("@annotation(top.kkuily.xingbackend.anotation.ApiSignAuth)")
    public Object checkApiSign(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        // 获取请求对象
        HttpServletRequest request = attributes.getRequest();
        // 获取方法体对象
        Method method = getMethod(joinPoint);
        if (method != null && !checkSign(method, request)) {
            throw new AccessDeniedException("Access denied");
        }
        return joinPoint.proceed();
    }

    /**
     * @param method  Method
     * @param request HttpServletRequest
     * @return boolean
     * @description API签名验证
     */
    private boolean checkSign(Method method, HttpServletRequest request) {
        ApiSignAuth apiSignAuth = method.getAnnotation(ApiSignAuth.class);
        boolean isNeedSign = apiSignAuth.value();
        // 判断是否需要验证，不需要验证则直接返回true
        if (!isNeedSign) {
            return true;
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        // 按键名对参数进行排序
        SortedSet<String> sortedKeys = new TreeSet<>(parameterMap.keySet());
        StringBuilder sortedParams = new StringBuilder();
        for (String key : sortedKeys) {
            sortedParams.append(key).append("=").append(parameterMap.get(key)[0]).append("&");
        }

        // 移除末尾的 "&" 字符
        sortedParams.setLength(sortedParams.length() - 1);

        // 获取请求头中的sign值
        String signValue = request.getHeader(SIGN_KEY_IN_HEADER);

        log.error("sortedParams: {}", sortedParams);
        log.error("signValue: {}", signValue);

        // patch
        if (signValue == null) {
            return false;
        }

        // 验证签名
        boolean isValid = false;
        try {
            isValid = ApiSignAuthUtils.verifySignature(
                    sortedParams.toString().replaceAll("\\n", ""),
                    signValue,
                    PUBLIC_KEY.replaceAll("\\s", "")
            );
        } catch (Exception e) {
            throw new RuntimeException("签名验证失败");
        }

        if (isValid) {
            // 判断是否被使用过，防止重放攻击
            String timestamp = stringRedisTemplate.opsForValue().get(NONCE_KEY_IN_CACHE + parameterMap.get("nonce")[0]);
            if (StringUtils.isEmpty(timestamp)) {
                // 判断是否在有效窗口内
                long parseTimestamp = Long.parseLong(parameterMap.get("timestamp")[0]);
                long diffTime = System.currentTimeMillis() - parseTimestamp;
                if (diffTime > LEGAL_TIME_PER_REQ) {
                    return false;
                }
                stringRedisTemplate
                        .opsForValue()
                        .set(
                                NONCE_KEY_IN_CACHE + parameterMap.get("nonce")[0],
                                parameterMap.get("timestamp")[0],
                                LEGAL_TIME_PER_REQ,
                                TimeUnit.MILLISECONDS
                        );
                return true;
            } else {
                // 重复使用
                return false;
            }
        }
        return false;
    }

    /**
     * @param joinPoint ProceedingJoinPoint
     * @return Method
     * @description 获取方法体对象
     */
    private Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
