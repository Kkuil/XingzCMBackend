package top.kkuily.xingbackend.model.po;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.kkuily.xingbackend.model.dto.response.admin.AdminAuthInfoResDTO;

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
    @TableId(type = IdType.ASSIGN_UUID)
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
    private String roleId;

    /**
     * 部门ID
     */
    private String deptId;

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
    private int gender;

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

    @TableField(exist = false)
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Admin转成本脱敏类的方法
     */
    public void convertTo(AdminAuthInfoResDTO adminAuthInfoResDto) {
        BeanUtils.copyProperties(this, adminAuthInfoResDto);
    }
}