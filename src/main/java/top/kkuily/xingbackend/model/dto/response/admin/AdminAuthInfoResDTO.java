package top.kkuily.xingbackend.model.dto.response.admin;

import lombok.Data;

import java.util.Date;

/**
 * @author 小K
 */
@Data
public class AdminAuthInfoResDTO {
    /**
     * 管理员账户ID（可用来登录）
     */
    private String id;

    /**
     * 管理员名称
     */
    private String name;

    /**
     * 管理员身份ID
     */
    private String roleId;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 手机号（例如：15712345674）
     */
    private String phone;

    /**
     * 默认头像
     */
    private String avatar;

    /**
     * 性别（0：女 1：男 2：未知）
     */
    private int gender;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）
     */
    private Date modifiedTime;

}
