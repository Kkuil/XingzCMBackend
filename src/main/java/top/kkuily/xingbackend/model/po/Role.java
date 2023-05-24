package top.kkuily.xingbackend.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import lombok.Data;

/**
 * @author 小K
 * @TableName role
 */
@TableName(value = "role")
@Data
public class Role {
    /**
     * 角色ID
     */
    @TableId
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