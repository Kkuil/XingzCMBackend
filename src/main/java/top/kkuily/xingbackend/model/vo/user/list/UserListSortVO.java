package top.kkuily.xingbackend.model.vo.user.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 小K
 * User表分页查询排序参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListSortVO {
    /**
     * 生日
     */
    private String birthday;
    /**
     * 创建时间
     */
    private String createdTime;
    /**
     * 修改时间
     */
    private String modifiedTime;
}
