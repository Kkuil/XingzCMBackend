package top.kkuily.xingbackend.aop;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.kkuily.xingbackend.anotation.FrequencyControl;
import top.kkuily.xingbackend.exception.BusinessException;
import top.kkuily.xingbackend.exception.CommonErrorEnum;
import top.kkuily.xingbackend.utils.RedisUtils;
import top.kkuily.xingbackend.utils.SpElUtils;
import top.kkuily.xingbackend.utils.Token;

import java.lang.reflect.Method;
import java.util.*;

import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_KEY_IN_HEADER;
import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_SECRET;

@Slf4j
@Aspect
@Component
public class FrequencyControlInterceptor {

    @Around("@annotation(top.kkuily.xingbackend.anotation.FrequencyControl)||@annotation(top.kkuily.xingbackend.anotation.FrequencyControlContainer)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取Method对象
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 获取请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        // 获取频控容器
        FrequencyControl[] frequencyContainer = method.getAnnotationsByType(FrequencyControl.class);
        // 初始化一个频控map
        Map<String, FrequencyControl> frequerncyMap = new HashMap<>();
        // 遍历频控容器
        for (int i = 0; i < frequencyContainer.length; i++) {
            // 获取频控对象
            FrequencyControl frequencyControl = frequencyContainer[i];
            // 获取频控前缀（默认方法限定名+注解排名（可能多个））
            String prefix =
                    StrUtil.isBlank(frequencyControl.prefixKey())
                            ? SpElUtils.getMethodKey(method) + ":index:" + i
                            : frequencyControl.prefixKey();
            // 获取频控目标
            String key = switch (frequencyControl.target()) {
                case EL -> SpElUtils.parseSpEl(method, joinPoint.getArgs(), frequencyControl.spEl());
                // 每个IP限制
                case IP -> request.getRemoteAddr();
                // 每个用户限制
                case UID -> {
                    // 获取TOKEN
                    String token = request.getHeader(USER_TOKEN_KEY_IN_HEADER);
                    // 解析TOKEN
                    Claims infoInToken;
                    try {
                        infoInToken = Token.parse(token, USER_TOKEN_SECRET);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    // 获取用户ID
                    String id = (String) infoInToken.get("id");
                    yield id;
                }
            };
            // 将频控对象放入map
            frequerncyMap.put(prefix + ":" + key, frequencyControl);
        }
        // 批量获取redis统计的值
        ArrayList<String> keyList = new ArrayList<>(frequerncyMap.keySet());
        List<Integer> countList = RedisUtils.mget(keyList, Integer.class);
        for (int i = 0; i < keyList.size(); i++) {
            // 获取redis中的所有key
            String key = keyList.get(i);
            // 获取redis中的所有value
            Integer count = countList.get(i);
            // 通过key获取频控对象
            FrequencyControl frequencyControl = frequerncyMap.get(key);
            if (Objects.nonNull(count) && count >= frequencyControl.count()) {
                // 超频
                log.warn("frequencyControl limit key:{},count:{}", key, count);
                throw new BusinessException(CommonErrorEnum.FREQUENCY_LIMIT);
            }
        }
        try {
            return joinPoint.proceed();
        } finally {
            // 不管成功还是失败，都增加次数
            frequerncyMap.forEach((k, v) -> {
                RedisUtils.inc(k, v.time(), v.unit());
            });
        }
    }
}
