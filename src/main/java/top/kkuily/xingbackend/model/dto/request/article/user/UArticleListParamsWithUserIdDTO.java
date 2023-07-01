package top.kkuily.xingbackend.model.dto.request.article.user;

import lombok.Data;

/**
 * @author 小K
 * @description 用户分页请求文章信息的DTO类
 */
@Data
public class UArticleListParamsWithUserIdDTO {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 文章状态ID
     */
    private Integer statusId;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 当前页大小
     */
    private Integer pageSize;
}
