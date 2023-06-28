package top.kkuily.xingbackend.model.dto.response.user;

import lombok.Data;

import java.util.Date;

/**
 * @author 小K
 * @Date 2020/12/22 16:04
 * @see top.kkuily.xingbackend.model.dto.response.user
 * @description 用户信息(用户个人中心)返回类
 */
@Data
public class UserInfoWithCenterResDTO {
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
    private String gender;

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
     * 标签
     */
    private String tags;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 加入了多少天
     */
    private Integer joinDays;

    /**
     * 是否为本人
     */
    private Boolean isSelf;
}
