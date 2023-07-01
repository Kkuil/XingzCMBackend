package top.kkuily.xingbackend.constant.commons;

/**
 * @description 错误类型
 * @author 小K
 */
public enum MsgType {
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

    MsgType(int i) {
        this.i = i;
    }

    public int getValue() {
        return this.i;
    }
}
