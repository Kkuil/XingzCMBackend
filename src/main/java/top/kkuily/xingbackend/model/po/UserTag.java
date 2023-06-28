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
 * @TableName user_tag
 */
@TableName(value ="user_tag")
@Data
public class UserTag implements Serializable {
    /**
     * 用户ID
     */
    @TableId
    private String id;

    /**
     * 标签ID
     */
    private Integer tagId;

    /**
     * 创建时间
     */
    private Date createdTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}