package top.kkuily.xingbackend.model.dto.request.user;

import lombok.Data;

/**
 * @author 小K
 * @description 用户登录携带的请求体
 */
@Data
public class UserLoginAccountBody {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证码
     */
    private String captcha;
    /**
     * 登录类型
     */
    private String type;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 短信验证码
     */
    private String smsCaptcha;
}
