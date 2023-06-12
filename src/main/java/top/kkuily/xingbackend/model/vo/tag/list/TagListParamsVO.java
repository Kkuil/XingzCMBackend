package top.kkuily.xingbackend.model.vo.tag.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.kkuily.xingbackend.model.vo.DateRangeCommonVO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 小K
 * Admin表分页查询的参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagListParamsVO {
    /**
     * 标签ID
     */
    private String id;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 当前页大小
     */
    private Integer pageSize;

    /**
     * 是否逻辑删除(0：未删除 1：已删除)
     */

    private String isDeleted;

    /**
     * 创建时间
     */
    private DateRangeCommonVO createdTime;

    /**
     * 最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）
     */
    private DateRangeCommonVO modifiedTime;

    /**
     * @return Map<String, Object>
     * @description bean对象转化为Map的方法
     */
    public Map<String, Object> beanToMapWithLimitField() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        return map;
    }
}
