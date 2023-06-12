package top.kkuily.xingbackend.constant.admin;

/**
 * @author 小K
 */
public class AdminInfo {
    /**
     * 管理员头像最大图片大小
     * 5 * 1024 * 1024
     */
    public static final Long ADMIN_MAX_AVATAR_SIZE = 5242880L;

    /**
     * 管理员头像在阿里云OSS上的存储路径
     */
    public static final String ADMIN_AVATAR_IN_ALIYUN_OSS_PATH = "resources/admin-avatars/";

    /**
     * 管理员名称默认前缀
     */
    public static final String ADMIN_DEFAULT_NAME_PREFIX = "xingz_cm_";

    /**
     * 管理员ID默认前缀
     */
    public static final String ADMIN_DEFAULT_ID_PREFIX = "xingz_cm_admin_";
}
