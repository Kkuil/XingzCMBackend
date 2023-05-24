package top.kkuily.xingbackend.model.po;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import lombok.Data;

/**
 * @author 小K
 * @TableName tag
 */
@TableName(value = "tag")
@Data
public class Tag {
    /**
     * 标签ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 标签名
     */
    private String tagName;

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
}