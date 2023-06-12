package top.kkuily.xingbackend.constant.commons;

/**
 * @author 小K
 * @description 请求相关常量
 */
public class Pattern {

    /**
     * 手机号正则校验
     */
    public static final String PHONE_REG = "1[3-9]\\d{9}";

    /**
     * 邮箱正则校验
     */
    public static final String EMAIL_REG = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
}
