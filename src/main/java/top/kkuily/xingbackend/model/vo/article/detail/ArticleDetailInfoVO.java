package top.kkuily.xingbackend.model.vo.article.detail;

import lombok.Data;

import java.util.Date;

/**
 * @author 小K
 * @description 用户获取文章详情中文章的详情实体
 */
@Data
public class ArticleDetailInfoVO {
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
     * 该用户是否已点赞
     */
    private Boolean isLiked;

    /**
     * 该用户是否已收藏
     */
    private Boolean isCollected;

    /**
     * 发布时间
     */
    private Date createdTime;

    /**
     * 修改时间时间
     */
    private Date modifiedTime;
}
