package top.kkuily.xingbackend.model.vo.article.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 小K
 * Admin表分页查询排序参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListSortVO {
    private String createdTime;
    private String modifiedTime;
}
