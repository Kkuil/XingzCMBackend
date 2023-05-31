package top.kkuily.xingbackend.model.dto.request.user;

import lombok.Data;

/**
 * @author 小K
 * @description 用户登录携带的请求体
 */
@Data
public class UserLoginAccountBodyDTO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}
