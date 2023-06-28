package top.kkuily.xingbackend.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName article_comment
 */
@TableName(value ="article_comment")
@Data
public class ArticleComment implements Serializable {
    /**
     * 评论ID
     */
    @TableId(type = IdType.AUTO)
    private Object id;

    /**
     * 文章ID
     */
    private String articleId;

    /**
     * 父级评论ID
     */
    private Object parentId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 喜欢数
     */
    private Object likedCount;

    /**
     * 不喜欢数
     */
    private Object dislikedCount;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}