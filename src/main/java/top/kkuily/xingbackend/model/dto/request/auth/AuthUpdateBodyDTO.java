package top.kkuily.xingbackend.model.dto.request.auth;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.kkuily.xingbackend.model.po.Auth;

import java.util.Date;

/**
 * @author 小K
 * @description 删除权限的传输数据
 */
@Data
public class AuthUpdateBodyDTO {
    /**
     * 权限ID
     */
    private String id;

    /**
     * 权限名
     */
    private String authName;

    /**
     * 权限路由
     */
    private String authRoute;

    /**
     * 权限相关描述
     */
    private String description;

    /**
     * @param auth Auth
     */
    public void convertTo(Auth auth) {
        BeanUtils.copyProperties(this, auth);
    }
}
