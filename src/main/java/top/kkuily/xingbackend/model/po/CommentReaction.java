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
 * @TableName comment_reaction
 */
@TableName(value ="comment_reaction")
@Data
public class CommentReaction implements Serializable {
    /**
     * 唯一标识每条记录的整数
     */
    @TableId(type = IdType.AUTO)
    private Object id;

    /**
     * 关联到评论的外键，表示该条点赞/不喜欢记录对应的评论
     */
    private Object commentId;

    /**
     * 关联到用户的外键，表示该条点赞/不喜欢记录的用户
     */
    private String userId;

    /**
     * 表示用户对评论的反应类型，例如点赞或不喜欢
     */
    private Object reaction;

    /**
     * 记录创建时间，用于记录用户点赞或表示不喜欢的时间戳
     */
    private Date createdTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}