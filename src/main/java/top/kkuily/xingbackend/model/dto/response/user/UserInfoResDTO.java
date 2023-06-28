package top.kkuily.xingbackend.model.dto.response.user;

import lombok.Data;

import java.util.Date;

/**
 * @author 小K
 */
@Data
public class UserInfoResDTO {
    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

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
     * 创建时间
     */
    private Date createdTime;

    /**
     * 最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）
     */
    private Date modifiedTime;

}
