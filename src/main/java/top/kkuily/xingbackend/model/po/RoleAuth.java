package top.kkuily.xingbackend.model.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName role_auth
 */
@TableName(value = "role_auth")
@Data
public class RoleAuth implements Serializable {
    /**
     * 权限ID（1000以上的整数）
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 权限名称
     */
    private String authname;

    /**
     * 与权限所绑定的路由（例如：['/userManage', '/adminManage']）
     */
    private String authroute;

    /**
     * 与权限所绑定的侧边栏（例如：['userManage:add', 'userManage:delete']）
     */
    private String authsidebar;

    /**
     * 关于该权限的描述
     */
    private String description;

    /**
     * 是否逻辑删除(0：未删除 1：已删除)
     */
    @TableLogic
    private Object isdeleted;

    /**
     * 创建时间
     */
    private Date createdtime;

    /**
     * 最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）
     */
    private Date modifiedtime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        RoleAuth other = (RoleAuth) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getAuthname() == null ? other.getAuthname() == null : this.getAuthname().equals(other.getAuthname()))
                && (this.getAuthroute() == null ? other.getAuthroute() == null : this.getAuthroute().equals(other.getAuthroute()))
                && (this.getAuthsidebar() == null ? other.getAuthsidebar() == null : this.getAuthsidebar().equals(other.getAuthsidebar()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getIsdeleted() == null ? other.getIsdeleted() == null : this.getIsdeleted().equals(other.getIsdeleted()))
                && (this.getCreatedtime() == null ? other.getCreatedtime() == null : this.getCreatedtime().equals(other.getCreatedtime()))
                && (this.getModifiedtime() == null ? other.getModifiedtime() == null : this.getModifiedtime().equals(other.getModifiedtime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAuthname() == null) ? 0 : getAuthname().hashCode());
        result = prime * result + ((getAuthroute() == null) ? 0 : getAuthroute().hashCode());
        result = prime * result + ((getAuthsidebar() == null) ? 0 : getAuthsidebar().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getIsdeleted() == null) ? 0 : getIsdeleted().hashCode());
        result = prime * result + ((getCreatedtime() == null) ? 0 : getCreatedtime().hashCode());
        result = prime * result + ((getModifiedtime() == null) ? 0 : getModifiedtime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", authname=").append(authname);
        sb.append(", authroute=").append(authroute);
        sb.append(", authsidebar=").append(authsidebar);
        sb.append(", description=").append(description);
        sb.append(", isdeleted=").append(isdeleted);
        sb.append(", createdtime=").append(createdtime);
        sb.append(", modifiedtime=").append(modifiedtime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}