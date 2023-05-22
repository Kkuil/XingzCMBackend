package top.kkuily.xingbackend.constant;

/**
 * @author 小K
 * @description 和登录相关的常量
 */
public class Login {
    /**
     * 响应头中的存token的key
     */
    public static final String TOKEN_KEY_IN_HEADER = "xingz_cm_admin_token";

    /**
     * token的生存时间ttl
     */
    public static final Long TOKEN_TTL = 2592000000L;

    /**
     * token 秘钥
     */
    public static final String TOKEN_SECRET = "xingz_cm_123456";

    /**
     * token 密码版本号
     */
    public static final String TOKEN_VERSION_KEY = "token:version:";
}
