package top.kkuily.xingbackend.model.enums;

/**
 * @author 小K
 * @description 权限
 */

public enum AuthEnums {
    /**
     * 管理员分页查询
     */
    ADMIN_LIST("admin_list", 1000),

    /**
     * 增加管理员
     */
    ADMIN_ADD("admin_add", 1001),

    /**
     * 删除管理员
     */
    ADMIN_DEL("admin_del", 1002),

    /**
     * 更新管理员
     */
    ADMIN_UPDATE("admin_update", 1003),

    /**
     * 查看管理员
     */
    ADMIN_CHECK("admin_check", 1004),

    /**
     * 用户分页查询
     */
    USER_LIST("user_list", 2000),

    /**
     * 增加用户
     */
    USER_ADD("user_add", 2001),

    /**
     * 删除用户
     */
    USER_DEL("user_del", 2002),

    /**
     * 更新用户
     */
    USER_UPDATE("user_update", 2003),

    /**
     * 查看用户
     */
    USER_CHECK("user_check", 2004),

    /**
     * 角色分页查询
     */
    ROLE_LIST("role_list", 3000),

    /**
     * 增加角色
     */
    ROLE_ADD("role_add", 3001),

    /**
     * 删除角色
     */
    ROLE_DEL("role_del", 3002),

    /**
     * 更新角色
     */
    ROLE_UPDATE("role_update", 3003),

    /**
     * 查看角色
     */
    ROLE_CHECK("role_check", 3004);

    private final String authName;
    private final int authId;

    AuthEnums(String authName, int authId) {
        this.authName = authName;
        this.authId = authId;
    }

    public String getAuthName() {
        return authName;
    }

    public int getAuthId() {
        return authId;
    }
}
