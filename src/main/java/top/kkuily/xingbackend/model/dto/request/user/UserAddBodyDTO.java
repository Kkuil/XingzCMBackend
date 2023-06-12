package top.kkuily.xingbackend.model.dto.request.user;

import lombok.Data;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.BeanUtils;
import top.kkuily.xingbackend.model.po.User;

import java.util.List;

/**
 * @author 小K
 * @description 增加管理员的DTO类
 */
@Data
public class UserAddBodyDTO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 性别
     */
    private String gender;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 标签IDs
     */
    private String[] tagIds;
    /**
     * 是否是会员
     */
    private String isVip;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 转换为本类方法
     *
     * @param user User
     */
    public void convertTo(User user) {
        BeanUtils.copyProperties(this, user);
        if (this.getTagIds() != null) {
            user.setTagIds(StringUtils.join(List.of(this.getTagIds()), ','));
        }
    }
}
