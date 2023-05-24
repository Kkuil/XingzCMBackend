package top.kkuily.xingbackend.constant.user;

/**
 * @author 小K
 * @description 和用户登录相关的常量
 */
public class Login {
    /**
     * 响应头中的存token的key
     */
    public static final String USER_TOKEN_KEY_IN_HEADER = "xingz_cm_user_token";

    /**
     * token的生存时间ttl
     */
    public static final Long USER_TOKEN_TTL = 2592000000L;

    /**
     * token 秘钥
     */
    public static final String USER_TOKEN_SECRET = "xingz_cm_user_123456";

    /**
     * token 密码版本号
     */
    public static final String USER_TOKEN_VERSION_KEY = "token:user:version:";
}
