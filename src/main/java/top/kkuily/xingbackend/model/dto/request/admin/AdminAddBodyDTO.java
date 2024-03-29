package top.kkuily.xingbackend.model.dto.request.admin;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.kkuily.xingbackend.model.po.Admin;

/**
 * @author 小K
 * @description 增加管理员的DTO类
 */
@Data
public class AdminAddBodyDTO {
    /**
     * 账户名
     */
    private String id;
    /**
     * 管理员名称
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 电话号
     */
    private String phone;
    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 转换为本类方法
     *
     * @param admin Admin
     */
    public void convertTo(Admin admin) {
        BeanUtils.copyProperties(this, admin);
    }
}
