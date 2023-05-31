package top.kkuily.xingbackend.model.vo.user.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.kkuily.xingbackend.model.vo.DateRangeCommonVO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 小K
 * Admin表分页查询的参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListParamsVO {

    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 生日（1970-01-01）
     */
    private Date birthday;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 创建时间
     */
    private DateRangeCommonVO createdTime;

    /**
     * 最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）
     */
    private DateRangeCommonVO modifiedTime;

    public Map<String, Object> beanToMapWithLimitField() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("username", username);
        map.put("birthday", birthday);
        map.put("phone", phone);
        map.put("email", email);
        return map;
    }
}
