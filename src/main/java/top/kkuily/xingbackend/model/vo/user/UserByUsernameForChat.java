package top.kkuily.xingbackend.model.vo.user;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.kkuily.xingbackend.model.po.User;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author 小K
 * @description 用户通过聊天搜索用户名获取用户信息实体
 */
@Data
public class UserByUsernameForChat {
    /**
     * 用户名
     */
    private String username;

    /**
     * 性别（0：女 1：男 2：未知）
     */
    private int gender;

    /**
     * 默认头像
     */
    private String avatar;

    /**
     * 是否为VIP用户（0：非会员 1：会员）
     */
    private String isVip;

    public static void userListConvertToChatList(List<User> listUser, List<UserByUsernameForChat> listChatInfo) {
        for (User user : listUser) {
            UserByUsernameForChat userByUsernameForChat = new UserByUsernameForChat();
            BeanUtils.copyProperties(user, userByUsernameForChat);
            // 复制User中的name和age属性到UserInfo中，同时忽略User中的password属性
            listChatInfo.add(userByUsernameForChat);
        }
    }
}
