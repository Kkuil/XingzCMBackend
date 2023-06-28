package top.kkuily.xingbackend.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import top.kkuily.xingbackend.model.dto.response.user.UserAuthInfoResDTO;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户ID
     */
    @TableId
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
    private Integer gender;

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
     * 头像
     */
    private String avatar;

    /**
     * 主页背景图
     */
    private String bgCover;

    /**
     * 是否为VIP用户（0：非会员 1：会员）
     */
    private Integer isVip;

    /**
     * 是否逻辑删除(0：未删除 1：已删除)
     */
    private Object isDeleted;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）
     */
    private Date modifiedTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public void convertTo(UserAuthInfoResDTO userAuthInfoResDto) {
        userAuthInfoResDto.setId(this.getId());
        userAuthInfoResDto.setUsername(this.getUsername());
        userAuthInfoResDto.setGender(this.getGender());
        userAuthInfoResDto.setBirthday(this.getBirthday());
        userAuthInfoResDto.setPhone(this.getPhone());
        userAuthInfoResDto.setEmail(this.getEmail());
        userAuthInfoResDto.setAvatar(this.getAvatar());
        userAuthInfoResDto.setIsVip(this.getIsVip());
        userAuthInfoResDto.setCreatedTime(this.getCreatedTime());
        userAuthInfoResDto.setModifiedTime(this.getModifiedTime());
    }
}