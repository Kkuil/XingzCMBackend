package top.kkuily.xingbackend.model.dto.request.role;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.kkuily.xingbackend.model.po.Role;

/**
 * @author 小K
 * @description 增加角色的DTO类
 */
@Data
public class RoleAddBodyDTO {
    /**
     * 角色名
     */
    private String roleName;

    /**
     * 权限列表
     */
    private String[] authIds;

    /**
     * 角色相关描述
     */
    private String description;

    /**
     * 转换为本类方法
     *
     * @param role Role
     */
    public void convertTo(Role role) {
        BeanUtils.copyProperties(this, role);
    }
}
