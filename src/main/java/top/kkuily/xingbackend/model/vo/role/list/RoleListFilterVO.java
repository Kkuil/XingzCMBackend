package top.kkuily.xingbackend.model.vo.role.list;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 小K
 * @description 角色过滤参数
 */
@Data
public class RoleListFilterVO implements Serializable {
    /**
     * 权限列表
     */
    private String[] authList;

    @Serial
    private static final long serialVersionUID = 1L;
}
