package top.kkuily.xingbackend.model.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author 小K
 * @TableName article_status
 */
@TableName(value = "article_status")
@Data
public class ArticleStatus implements Serializable {
    /**
     * 文章状态ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 状态名
     */
    private String statusName;

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
    private static final long serialVersionUID = 1L;
}