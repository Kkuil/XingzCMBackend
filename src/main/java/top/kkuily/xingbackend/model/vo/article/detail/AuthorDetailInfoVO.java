package top.kkuily.xingbackend.model.vo.article.detail;

import lombok.Data;
import top.kkuily.xingbackend.model.po.User;

import java.util.Date;
import java.util.List;

/**
 * @author 小K
 * @description 用户获取文章详情中用户的详情实体
 */
@Data
public class AuthorDetailInfoVO {

    /**
     * 作者ID
     */
    private String id;

    /**
     * 作者名
     */
    private String username;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 电话
     */
    private String phone;

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
    private List<Integer> tagIds;

    /**
     * 是否是VIP
     */
    private Integer isVip;

    public void allSet(User user) {
        this.setId(user.getId());
        this.setUsername(user.getUsername());
        this.setBirthday(user.getBirthday());
        this.setGender(user.getGender());
        this.setEmail(user.getEmail());
        this.setIsVip(user.getIsVip());
    }
}
