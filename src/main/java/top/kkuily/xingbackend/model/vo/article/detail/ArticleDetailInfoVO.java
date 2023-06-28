package top.kkuily.xingbackend.model.vo.article.detail;

import java.util.Date;

/**
 * @author 小K
 * @description 用户获取文章详情中文章的详情实体
 */
public class ArticleDetailInfo {
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

    /**
     * 修改时间时间
     */
    private Date modifiedTime;
}
