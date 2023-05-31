package top.kkuily.xingbackend.model.vo.role.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.kkuily.xingbackend.model.vo.DateRangeCommonVO;

/**
 * @author 小K
 * Admin表分页查询的参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleListParamsVO {
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
    private String authList;

    /**
     * 角色相关描述
     */
    private String description;


    /**
     * 当前页
     */
    private int current;
    /**
     * 当前页大小
     */
    private int pageSize;

    /**
     * 是否逻辑删除(0：未删除 1：已删除)
     */

    private String isDeleted;

    /**
     * 创建时间
     */
    private DateRangeCommonVO createdTime;

    /**
     * 最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）
     */
    private DateRangeCommonVO modifiedTime;
}
