package top.kkuily.xingbackend.model.po;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author 小K
 * @TableName user_article
 */
@TableName(value = "user_article")
@Data
public class UserArticle implements Serializable {
    /**
     * 用户ID
     */
    @TableId
    private String id;

    /**
     * 已发布文章数（例如：[1, 2, 3]记录了已发布的文章的ID）
     */
    private Object published;

    /**
     * 点赞信息（例如：[1, 2, 3]记录了点赞了的文章的ID）
     */
    private Object liked;

    /**
     * 收藏信息（例如：[1, 2, 3]记录了收藏了的文章的ID）
     */
    private Object collected;

    /**
     * 是否逻辑删除(0：未删除 1：已删除)
     */

    private String isDeleted;

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