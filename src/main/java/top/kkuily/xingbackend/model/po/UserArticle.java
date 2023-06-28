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
 * @TableName user_article
 */
@TableName(value ="user_article")
@Data
public class UserArticle implements Serializable {
    /**
     * 用户ID
     */
    @TableId
    private String id;

    /**
     * 文章ID
     */
    private String articleId;

    /**
     * 对于用户来说这篇文章的类型，例如：喜欢，收藏，自己发布
     */
    private Object type;

    /**
     * 创建时间
     */
    private Date createdTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}