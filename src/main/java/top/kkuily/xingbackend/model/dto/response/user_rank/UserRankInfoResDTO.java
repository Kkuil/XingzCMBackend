package top.kkuily.xingbackend.model.dto.response.user_rank;

import lombok.Data;

/**
 * @author 小K
 * @description 用户等级信息
 */
@Data
public class UserRankInfoResDTO {
    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 是否为VIP用户（0：非会员 1：会员）
     */
    private String isVip;

    /**
     * 用户等级
     */
    private Integer level;

    /**
     * 用户积分
     */
    private Integer points;

}
