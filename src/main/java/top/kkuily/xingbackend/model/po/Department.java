package top.kkuily.xingbackend.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import lombok.Data;

/**
 * @author 小K
 * @TableName department
 */
@TableName(value = "department")
@Data
public class Department {
    /**
     * 部门ID
     */
    @TableId
    private String id;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 管理员ID
     */
    private String managerId;

    /**
     * 地区ID
     */
    private Integer locationId;

    /**
     * 是否逻辑删除(0：未删除 1：已删除)
     */
    
    private String isDeleted;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）
     */
    private Date modifiedTime;
}