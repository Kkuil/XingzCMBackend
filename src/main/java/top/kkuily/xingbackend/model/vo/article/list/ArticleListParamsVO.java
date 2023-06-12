package top.kkuily.xingbackend.model.vo.article.list;

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
public class ArticleListParamsVO {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 状态ID
     */
    private String statusId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

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
        map.put("title", title);
        map.put("content", content);
        return map;
    }
}
