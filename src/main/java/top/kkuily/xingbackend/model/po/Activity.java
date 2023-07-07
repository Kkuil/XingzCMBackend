package top.kkuily.xingbackend.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName activity
 */
@TableName(value ="activity")
@Data
public class Activity implements Serializable {
    /**
     * 活动ID
     */
    @TableId
    private String id;

    /**
     * 活动发起者ID
     */
    private String activatorId;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动详情
     */
    private String detail;

    /**
     * 活动封面
     */
    private String cover;

    /**
     * 活动类型
     */
    private String type;

    /**
     * 活动规则
     */
    private String rule;

    /**
     * 备注
     */
    private String ps;

    /**
     * 活动开始时间
     */
    private Date startTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 参与人数上限
     */
    private Integer maxAttendance;

    /**
     * 谁可参与，1为全部，2为会员
     */
    private Integer authorizedPersonnel;

    /**
     * 最大可获得星分币数，-1为无上限
     */
    private Integer maxAquirableXingCoin;

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