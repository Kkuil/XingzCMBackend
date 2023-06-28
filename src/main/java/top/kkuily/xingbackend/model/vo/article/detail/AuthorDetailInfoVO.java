package top.kkuily.xingbackend.model.vo.article.detail;

import lombok.Data;

import java.util.Date;

/**
 * @author 小K
 * @description 用户获取文章详情中用户的详情实体
 */
@Data
public class UserDetailInfoVO {
    /**
     * 作者ID
     */
    private String id;

    /**
     * 作者名
     */
    private String username;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户标签
     */
    private String tagIds;

    /**
     * 是否是VIP
     */
    private Integer isVip;
}
