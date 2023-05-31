package top.kkuily.xingbackend.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author 小K
 * @TableName user_rank
 */
@TableName(value = "user_rank")
@Data
public class UserRank implements Serializable {
    /**
     * 用户ID
     */
    @TableId
    private String id;

    /**
     * 星分大小（每100星分为一等级）
     */
    private Integer points;

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