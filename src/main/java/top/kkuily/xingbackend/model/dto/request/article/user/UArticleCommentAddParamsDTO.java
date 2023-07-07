package top.kkuily.xingbackend.model.dto.request.article.user;

import lombok.Data;

/**
 * @author 小K
 * @description 用户评论实体
 */
@Data
public class UArticleCommentAddParamsDTO {
    /**
     * 文章ID
     */
    private String articleId;

    /**
     * 父级评论ID
     */
    private Integer parentId;

    /**
     * 评论内容
     */
    private String content;

}
