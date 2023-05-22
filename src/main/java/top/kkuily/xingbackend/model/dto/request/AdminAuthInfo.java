package top.kkuily.xingbackend.model.dto.request;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import top.kkuily.xingbackend.model.po.Admin;
import top.kkuily.xingbackend.model.po.Role;

import java.util.List;

/**
 * @author 小K
 * @description 管理员身份验证返回信息
 */
@Data
public class AdminAuthInfo {

    /**
     * 管理员账户ID（可用来登录）
     */
    @TableId
    private String id;

    /**
     * 管理员名称
     */
    private String name;

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
    private Object gender;

    /**
     * 角色名
     */
    private String rolename;

    /**
     * 角色所拥有的权限路由（例如：['/userManage', '/adminManage']）
     */
    private List<String> authroutes;

    /**
     * 角色所拥有的侧边栏（例如：['userManage:add', 'userManage:delete']）
     */
    private List<String> authsidebars;

    public void setAdminAuthInfo(Admin admin, Role role) {
        this.setId(admin.getId());
        this.setName(admin.getName());
        this.setPhone(admin.getPhone());
        this.setAvatar(admin.getAvatar());
        this.setGender(admin.getGender());
        this.setRolename(role.getRolename());
    }
}
