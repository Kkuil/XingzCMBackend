package top.kkuily.xingbackend.model.vo.chat_gpt.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.kkuily.xingbackend.model.vo.DateRangeCommonVO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 小K
 * @description ChatGPT表分页查询的参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGPTModelListParamsVO {
    /**
     * ChatGPTID
     */
    private String id;

    /**
     * ChatGPT名
     */
    private String name;

    /**
     * ChatGPT相关描述
     */
    private String description;

    /**
     * ChatGPT封面
     */
    private String cover;

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
        map.put("name", name);
        map.put("cover", cover);
        map.put("description", description);
        return map;
    }
}
