package top.kkuily.xingbackend.model.po;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName article_statistic
 */
@TableName(value = "article_statistic")
@Data
public class ArticleStatistic implements Serializable {
    /**
     * 文章ID
     */
    @TableId
    private String id;

    /**
     * 喜欢文章的用户ID
     */
    private String liked;

    /**
     * 收藏文章的用户ID
     */
    private String collected;

    /**
     * 评论文章的ID
     */
    private String commentId;

    /**
     * 是否逻辑删除(0：未删除 1：已删除)
     */
    private Object isDeleted;

    /**
     * 创建时间
     */
      private Date createdTime;

    /**
     * 最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）
     */
    private Date modifiedTime;

    @TableField(exist = false)
    @Serial
    private static final long serialVersionUID = 1L;
}