package top.kkuily.xingbackend.model.vo.user.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 小K
 * User表分页查询排序参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListSortVo {
    /**
     * 创建时间
     */
    private String createdTime;
    /**
     * 修改时间
     */
    private String modifiedTime;
}
