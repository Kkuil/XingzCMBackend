package top.kkuily.xingbackend.model.dto.request.dept;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.kkuily.xingbackend.model.po.Department;
import top.kkuily.xingbackend.model.po.Role;

import java.util.Date;

/**
 * @author 小K
 * @description 删除管理员的传输数据
 */
@Data
public class DeptUpdateBodyDTO {

    /**
     * 部门ID
     */
    private String id;

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
     * @param dept Department
     */
    public void convertTo(Department dept) {
        BeanUtils.copyProperties(this, dept);
    }
}
