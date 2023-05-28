package top.kkuily.xingbackend.model.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import top.kkuily.xingbackend.model.enums.Gender;

/**
 * @author 小K
 * @TableName user
 */
@TableName(value = "user")
@Data
public class User implements Serializable {
    /**
     * 用户ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 性别（0：女 1：男 2：未知）
     */
    private int gender;

    /**
     * 生日（1970-01-01）
     */
    private Date birthday;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 标签（例如：[1，2]）
     */
    private String tagIds;

    /**
     * 默认头像
     */
    private String avatar;

    /**
     * 是否为VIP用户（0：非会员 1：会员）
     */
    private String isVip;

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