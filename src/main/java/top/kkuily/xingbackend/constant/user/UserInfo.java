package top.kkuily.xingbackend.constant.user;

/**
 * @author 小K
 */
public class UserInfo {
    /**
     * 用户头像最大图片大小
     * 5 * 1024 * 1024
     */
    public static final Long USER_MAX_AVATAR_SIZE = 5242880L;

    /**
     * 用户头像在阿里云OSS上的存储路径
     */
    public static final String USER_AVATAR_IN_ALIYUN_OSS_PATH = "resources/user-avatars/";

    /**
     * 用户名称默认前缀
     */
    public static final String USER_DEFAULT_NAME_PREFIX = "xingz_cm_";
}
