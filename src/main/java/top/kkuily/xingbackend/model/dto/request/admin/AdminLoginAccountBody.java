package top.kkuily.xingbackend.model.dto.request.admin;

import lombok.Data;

/**
 * @author 小K
 * @description 管理员登录接受的参数
 */
@Data
public class AdminLoginAccountBody {
    /**
     * 账户id
     */
    private String id;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否自动登录
     */
    private Boolean autoLogin;

    /**
     * 登录类型
     */
    private String type;
}
