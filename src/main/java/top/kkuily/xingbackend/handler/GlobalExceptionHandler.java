package top.kkuily.xingbackend.handler;

import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author 小K
 * @description 全局异常处理器
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @param e Exception
     * @return ResponseEntity
     * @description 处理所有不可知的异常
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    /**
     * @param e Exception
     * @return ResponseEntity
     * @description 处理未登录异常
     */
    @ExceptionHandler(value = AccessException.class)
    public ResponseEntity handleAuthException(Exception e) {
        // 处理异常的逻辑
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }
}