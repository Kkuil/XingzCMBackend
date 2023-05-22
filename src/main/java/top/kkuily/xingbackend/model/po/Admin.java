package top.kkuily.xingbackend.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author 小K
 * @TableName admin
 */
@TableName(value = "admin")
@Data
public class Admin implements Serializable {
    /**
     * 管理员账户ID（可用来登录）
     */
    @TableId
    private String id;

    /**
     * 管理员名称
     */
    private String name;

    /**
     * 管理员密码
     */
    private String password;

    /**
     * 管理员身份ID
     */
    private String roleid;

    /**
     * 部门ID
     */
    private String deptid;

    /**
     * 手机号（例如：15712345674）
     */
    private String phone;

    /**
     * 默认头像
     */
    private String avatar;

    /**
     * 性别（0：女 1：男 2：未知）
     */
    private String gender;

    /**
     * 是否逻辑删除(0：未删除 1：已删除)
     */
    @TableLogic
    private String isdeleted;

    /**
     * 创建时间
     */
    private Date createdtime;

    /**
     * 最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）
     */
    private Date modifiedtime;
}