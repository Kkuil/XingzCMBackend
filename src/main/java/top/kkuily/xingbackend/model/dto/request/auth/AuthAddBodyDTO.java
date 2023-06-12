package top.kkuily.xingbackend.model.dto.request.auth;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.kkuily.xingbackend.model.po.Auth;

/**
 * @author 小K
 * @description 增加文章的DTO类
 */
@Data
public class AuthAddBodyDTO {
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
     * 转换为本类方法
     *
     * @param auth Auth
     */
    public void convertTo(Auth auth) {
        BeanUtils.copyProperties(this, auth);
    }
}
