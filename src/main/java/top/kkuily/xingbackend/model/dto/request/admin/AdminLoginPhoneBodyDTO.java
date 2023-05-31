package top.kkuily.xingbackend.model.dto.request.admin;

import lombok.Data;

/**
 * @author 小K
 * @description 管理员登录接受的参数
 */
@Data
public class AdminLoginPhoneBodyDTO {
    /**
     * 手机号
     */
    private String phone;
    /**
     * 短信验证码
     */
    private String sms;
}
