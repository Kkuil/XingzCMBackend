package top.kkuily.xingbackend.aop;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import jakarta.servlet.http.HttpServletRequest;
import top.kkuily.xingbackend.model.po.Log;
import top.kkuily.xingbackend.service.ILogService;

import java.util.UUID;

/**
 * @author Kkuil
 * @description 请求响应日志 AOP
 **/
@Aspect
@Component
@Slf4j
public class LogInterceptor {

    @Resource
    private HttpServletRequest httpServletRequest;

    @Resource
    private ILogService logService;

    /**
     * @param point ProceedingJoinPoint
     * @return Object
     * @throws Throwable Throwable
     * @description 执行拦截
     */
    @Around("execution(* top.kkuily.xingbackend.web.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 生成请求唯一 id
        String id = StringUtils.substring(UUID.randomUUID().toString(), 0, 10);
        String path = httpServletRequest.getRequestURI();
        // 请求方法
        String method = httpServletRequest.getMethod();
        // 获取请求参数
        Object[] args = point.getArgs();
        String params = "[" + StringUtils.join(args, ", ") + "]";
        // 获取请求IP
        String ip = httpServletRequest.getRemoteHost();
        // 执行原方法
        Object result = point.proceed();
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        // 输出请求日志
        log.info("id: {}, path: {}, method: {}, ip: {}, params: {}, cost: {}",
                id,
                path,
                method,
                ip,
                params,
                totalTimeMillis
        );
        // 日志上报
        Log logEntity = new Log();
        logEntity.setId(id);
        logEntity.setPath(path);
        logEntity.setMethod(method);
        logEntity.setIp(ip);
        logEntity.setParams(params);
        logEntity.setTotalTime(String.valueOf(totalTimeMillis));
        logService.save(logEntity);
        return result;
    }
}

