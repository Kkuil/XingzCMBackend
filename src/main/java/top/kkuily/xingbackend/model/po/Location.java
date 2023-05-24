package top.kkuily.xingbackend.model.po;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import lombok.Data;

/**
 * @TableName location
 */
@TableName(value = "location")
@Data
public class Location {
    /**
     * 地区ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 街道
     */
    private String street;

    /**
     * 城市
     */
    private String city;

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