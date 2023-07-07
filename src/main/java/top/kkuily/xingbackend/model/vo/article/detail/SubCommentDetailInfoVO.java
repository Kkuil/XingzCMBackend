package top.kkuily.xingbackend.model.vo.article.detail;

import lombok.Data;

import java.util.Date;

/**
 * @author 小K
 * @description 用户评论ID获取子级评论的详情实体
 */
@Data
public class SubCommentDetailInfoVO {
    /**
     * 评论ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 回复的ID
     */
    private String parentId;

    /**
     * 回复的用户ID
     */
    private String parentUserId;

    /**
     * 回复的用户名
     */
    private String parentUsername;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 请求者是否点赞
     */
    private Boolean isLiked;

    /**
     * 请求者是否不喜欢
     */
    private Boolean isDisliked;

    /**
     * 喜欢数
     */
    private Integer likedCount;

    /**
     * 不喜欢数
     */
    private Integer dislikedCount;

    /**
     * 子评论数
     */
    private Integer subCommentCount;

    /**
     * 创建时间
     */
    private Date createdTime;

}
