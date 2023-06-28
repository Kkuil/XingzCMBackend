package top.kkuily.xingbackend.model.dto.request.article.user;

import lombok.Data;

/**
 * @author 小K
 * @description 用户分页请求文章信息的DTO类
 */
@Data
public class UArticleListParamsDTO {
    /**
     * 标签
     */
    private Integer tagId;

    /**
     * 分类
     */
    private String categoryId;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 当前页大小
     */
    private Integer pageSize;
}
