package top.kkuily.xingbackend.model.dto.response.article.user;

import lombok.Data;

import java.util.Date;

/**
 * @author 小K
 * @description 文章分页查询返回类
 */
@Data
public class ArticleInfoResWithUserDTO {
    /**
     * 文章ID
     */
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

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
     * 是否被喜欢
     */
    private Boolean isLiked;

    /**
     * 是否被收藏
     */
    private Boolean isCollected;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 发布时间
     */
    private Date createdTime;

}
