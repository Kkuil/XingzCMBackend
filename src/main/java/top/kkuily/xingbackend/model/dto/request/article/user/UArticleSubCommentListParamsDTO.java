package top.kkuily.xingbackend.model.dto.request.article.user;

import lombok.Data;

/**
 * @author 小K
 * @description 分页请求文章子评论信息的DTO类
 */
@Data
public class UArticleSubCommentListParamsDTO {
    /**
     * 评论ID
     */
    private Integer commentId;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 当前页大小
     */
    private Integer pageSize;
}
