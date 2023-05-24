package top.kkuily.xingbackend.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author 小K
 */
@Component
@Slf4j
public class GlobalInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("global");
        String parameterTransmissionMethod = request.getHeader("x-param-type");
        switch (parameterTransmissionMethod) {
            case "Path": {
                return verifyWithPath(request);
            }
            case "Params": {
                return verifyWithParams(request);
            }
            case "Body": {
                return verifyWithBody(request);
            }
            default: {
                return false;
            }
        }
    }

    /**
     * 对使用Path传参的接口进行验证
     * @param request HttpServletRequest
     * @return Boolean
     */
    private Boolean verifyWithPath(HttpServletRequest request) {
        return true;
    }

    /**
     * 对使用Params传参的接口进行验证
     * @param request HttpServletRequest
     * @return Boolean
     */
    private Boolean verifyWithParams(HttpServletRequest request) {
        return true;
    }

    /**
     * 对使用Body传参的接口进行验证
     * @param request HttpServletRequest
     * @return Boolean
     */
    private Boolean verifyWithBody(HttpServletRequest request) {
        return true;
    }

}
