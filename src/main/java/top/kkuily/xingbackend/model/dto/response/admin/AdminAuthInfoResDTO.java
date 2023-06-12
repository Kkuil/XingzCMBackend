package top.kkuily.xingbackend.model.dto.response.admin;

import cn.hutool.core.date.DateTime;
import lombok.Data;

import java.util.Date;
import java.util.List;

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
     * 角色名
     */
    private String roleDescription;

    /**
     * 角色所拥有的权限路由（例如：['user-manage', 'admin-manage']）
     */
    private String authRoutes;

    /**
     * 部门名
     */
    private String deptName;

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
    private Integer gender;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）
     */
    private Date modifiedTime;

}
