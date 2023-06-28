package top.kkuily.xingbackend.model.enums;

/**
 * @author 小K
 * @description 权限
 */

public enum AUTHEnums {
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
    ADMIN_CHECK("admin_check", 1004);

    private final String authName;
    private final int authId;

    AUTHEnums(String authName, int authId) {
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
