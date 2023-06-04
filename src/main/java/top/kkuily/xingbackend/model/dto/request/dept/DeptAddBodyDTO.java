package top.kkuily.xingbackend.model.dto.request.dept;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.kkuily.xingbackend.model.po.Department;

/**
 * @author 小K
 * @description 增加角色的DTO类
 */
@Data
public class DeptAddBodyDTO {
    /**
     * 部门名
     */
    private String deptName;

    /**
     * 部门管理员ID
     */
    private String managerId;

    /**
     * 部门位置ID
     */
    private String locationId;

    /**
     * 转换为本类静态方法
     *
     * @param dept Department
     */
    public void convertTo(Department dept) {
        BeanUtils.copyProperties(this, dept);
    }
}
