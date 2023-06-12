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
 * @TableName article
 */
@TableName(value ="article")
@Data
public class Article implements Serializable {
    /**
     * 文章ID
     */
    @TableId
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 状态ID
     */
    private Integer statusId;

    /**
     * 分类
     */
    private String categoryIds;

    /**
     * 文章封面图
     */
    private String cover;

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