package top.kkuily.xingbackend.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName article_category
 */
@TableName(value ="article_category")
@Data
public class ArticleCategory implements Serializable {
    /**
     * 文章ID
     */
    @TableId
    private String id;

    /**
     * 分类ID
     */
    private String categoryId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}