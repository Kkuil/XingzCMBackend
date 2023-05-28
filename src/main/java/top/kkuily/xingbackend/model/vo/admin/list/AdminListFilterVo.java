package top.kkuily.xingbackend.model.vo.admin.list;

import lombok.Data;

/**
 * @author 小K
 * @description 管理员过滤参数
 */
@Data
public class AdminListFilterVo {
    /**
     * 性别
     */
    private String gender;
    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 部门ID
     */
    private String deptId;
    /**
     * 是否逻辑删除
     */
    private String isDeleted;
}
