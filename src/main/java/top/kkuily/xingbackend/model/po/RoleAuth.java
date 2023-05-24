package top.kkuily.xingbackend.model.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author 小K
 * @TableName role_auth
 */
@TableName(value = "role_auth")
@Data
public class RoleAuth {
    /**
     * 权限ID（1000以上的整数）
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 权限名称
     */
    private String authName;

    /**
     * 与权限所绑定的路由（例如：['/userManage', '/adminManage']）
     */
    private String authRoute;

    /**
     * 与权限所绑定的侧边栏（例如：['userManage:add', 'userManage:delete']）
     */
    private String authSideBar;

    /**
     * 关于该权限的描述
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