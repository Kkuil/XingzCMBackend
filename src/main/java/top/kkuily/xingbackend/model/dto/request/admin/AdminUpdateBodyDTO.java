package top.kkuily.xingbackend.model.dto.request.admin;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.kkuily.xingbackend.model.po.Admin;

/**
 * @author 小K
 * @description 删除管理员的传输数据
 */
@Data
public class AdminUpdateBodyDTO {
    /**
     * 管理员账号
     */
    private String id;

    /**
     * 管理员密码
     */
    private String password;

    /**
     * 管理员名称
     */
    private String name;

    /**
     * 管理员角色ID
     */
    private String roleId;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 部门号
     */
    private String deptId;

    /**
     * 性别
     */
    private int gender;

    public void convertTo(Admin admin) {
        BeanUtils.copyProperties(this, admin);
    }
}
