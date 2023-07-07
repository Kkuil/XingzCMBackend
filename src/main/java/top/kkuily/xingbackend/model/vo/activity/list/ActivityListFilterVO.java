package top.kkuily.xingbackend.model.vo.activity.list;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 小K
 * @description 管理员过滤参数
 */
@Data
public class ActivityListFilterVO implements Serializable {
    /**
     * 性别
     */
    private String[] gender;
    /**
     * 角色ID
     */
    private String[] roleId;
    /**
     * 部门ID
     */
    private String[] deptId;
    /**
     * 是否逻辑删除
     */
    private String[] isDeleted;

    @Serial
    private static final long serialVersionUID = 1L;
}
