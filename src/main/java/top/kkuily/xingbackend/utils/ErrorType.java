package top.kkuily.xingbackend.utils;

/**
 * @description 错误类型
 * @author 小K
 */
public enum ErrorType {
    /**
     * 静默控制台输出
     */
    SILENT(0),
    /**
     * 警告
     */
    WARN_MESSAGE(1),
    /**
     * 错误
     */
    ERROR_MESSAGE(2),
    /**
     * 弹窗通知
     */
    NOTIFICATION(3),
    /**
     * 重定向
     */
    REDIRECT(9);

    private int i;

    ErrorType(int i) {
        this.i = i;
    }
}
