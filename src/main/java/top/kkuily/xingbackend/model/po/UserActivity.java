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
 * @TableName user_activity
 */
@TableName(value ="user_activity")
@Data
public class UserActivity implements Serializable {
    /**
     * 用户ID
     */
    private String id;

    /**
     * 活动ID
     */
    private String activityId;

    /**
     * 已获得的星分币数
     */
    private Integer coin;

    /**
     * 完成时间
     */
    private Date completeTime;

    /**
     * 是否逻辑删除(0：未删除 1：已删除)
     */
    private Object isDeleted;

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