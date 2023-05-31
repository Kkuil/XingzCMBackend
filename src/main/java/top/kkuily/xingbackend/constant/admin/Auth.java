package top.kkuily.xingbackend.constant.admin;

/**
 * @author 小K
 * @description 和管理员登录相关的常量
 */
public class Auth {
    /**
     * 响应头中的存token的key
     */
    public static final String ADMIN_TOKEN_KEY_IN_HEADER = "xingz_cm_admin_token";

    /**
     * token的生存时间ttl
     */
    public static final Long ADMIN_TOKEN_TTL = 2592000000L;

    /**
     * token 秘钥
     */
    public static final String ADMIN_TOKEN_SECRET = "xingz_cm_admin_123456";

    /**
     * token 密码版本号
     */
    public static final String ADMIN_TOKEN_VERSION_KEY = "token:admin:version:";
    /**
     * 管理员使用手机号发送验证码进行登录的验证码存储key
     */
    public static final String ADMIN_SMS_CACHE_KEY = "admin:sms:";
}
