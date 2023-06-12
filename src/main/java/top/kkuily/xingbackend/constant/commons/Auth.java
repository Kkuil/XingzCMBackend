package top.kkuily.xingbackend.constant.commons;

import lombok.Data;

/**
 * @author 小K
 * @description 验证相关常量
 */
@Data
public class Auth {
    /**
     * 非法字符
     */
    public static final String ILLEGAL_CHAR = "'\";\\<>\\/|*%():,+&^{}[]?";
    /**
     * 用户主账号
     */
    public static final String USER_MAIN_ID = "477f3c9feda7e6303190a1381cd49d68";
    /**
     * 基础角色
     */
    public static final String[] BASE_ROLE = {"1", "2"};
    /**
     * 基础ChatGPT模型
     */
    public static final String[] BASE_CHATGPT_MODEL = {"1651468516836098050"};
    /**
     * 存于请求头中的sign key值
     */
    public static final String SIGN_KEY_IN_HEADER = "x-api-sign";
}
