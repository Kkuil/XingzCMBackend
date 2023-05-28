package top.kkuily.xingbackend.model.vo.admin.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.kkuily.xingbackend.model.vo.DateRangeCommonVo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 小K
 * @description 管理员表分页查询的参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminListParamsVo {
    /**
     * 管理员账户ID（可用来登录）
     */
    private String id;

    /**
     * 管理员名称
     */
    private String name;


    /**
     * 手机号（例如：15712345674）
     */
    private String phone;

    /**
     * 创建时间
     */
    private DateRangeCommonVo createdTime;

    /**
     * 最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）
     */
    private DateRangeCommonVo modifiedTime;

    public Map<String, Object> beanToMapWithLimitField() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("phone", phone);
        return map;
    }
}
