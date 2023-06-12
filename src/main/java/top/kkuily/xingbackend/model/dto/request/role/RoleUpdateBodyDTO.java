package top.kkuily.xingbackend.model.dto.request.role;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import top.kkuily.xingbackend.model.po.Admin;
import top.kkuily.xingbackend.model.po.Role;

import java.util.Date;

/**
 * @author 小K
 * @description 更新角色的传输数据
 */
@Data
public class RoleUpdateBodyDTO {

    /**
     * 角色ID
     */
    private String id;
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
     * @param role Role
     */
    public void convertTo(Role role) {
        BeanUtils.copyProperties(this, role);
    }
}
