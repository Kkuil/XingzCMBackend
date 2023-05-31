package top.kkuily.xingbackend.model.dto.request.user;

import lombok.Data;

/**
 * @author 小K
 * @description 用户使用手机号登录
 */
@Data
public class UserLoginPhoneBodyDTO {
    /**
     * 手机号
     */
    private String phone;
    /**
     * 短信验证码
     */
    private String sms;
}
