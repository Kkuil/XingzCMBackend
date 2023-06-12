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
 * @author 小K
 * @TableName article_category
 */
@TableName(value ="article_category")
@Data
public class ArticleCategory implements Serializable {
    /**
     * 分类ID
     */
    @TableId
    private String id;

    /**
     * 分类名
     */
    private String categoryName;

    /**
     * 创建时间（评论时间）
     */
    private Date createdTime;

    /**
     * 最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）
     */
    private Date modifiedTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}