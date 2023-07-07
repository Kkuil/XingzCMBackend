package top.kkuily.xingbackend.model.vo.activity.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author 小K
 * @description 管理员表分页查询排序参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityListSortVO {
    /**
     * 创建时间
     */
    private String createdTime;
    /**
     * 修改时间
     */
    private String modifiedTime;
}
