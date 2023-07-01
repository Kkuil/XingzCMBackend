package top.kkuily.xingbackend.model.dto.response.article.user;

import lombok.Data;

import java.util.Date;

/**
 * @author 小K
 * @description 通过用户ID获取文章分页查询返回类
 */
@Data
public class ArticleInfoResWithUserProfileDTO {
    /**
     * 文章ID
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 文章封面图
     */
    private String cover;

    /**
     * 喜欢数
     */
    private Integer likedCount;

    /**
     * 收藏数
     */
    private Integer collectedCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 发布时间
     */
    private Date createdTime;
}
