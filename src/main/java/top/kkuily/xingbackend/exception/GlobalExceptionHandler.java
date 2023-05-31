package top.kkuily.xingbackend.exception;

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
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handleException(Exception e) {
        // 处理异常的逻辑
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}