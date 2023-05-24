package top.kkuily.xingbackend.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName article
 */
@TableName(value = "article")
@Data
public class Article implements Serializable {
    /**
     * 文章ID
     */
    @TableId
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 评论信息（例如：'{id: '1', comment: '欢迎', 'createdTime': '1970-01-01 00:00:00', 'modifiedTime': '1970-01-01 00:00:00'}'）
     */
    private String remark;

    /**
     * 状态ID
     */
    private Integer statusId;

    /**
     * 文章封面图
     */
    private String cover;

    /**
     * 点赞信息（例如：[1, 2, 3]记录了点赞了该文章的星友ID）
     */
    private Object liked;

    /**
     * 收藏信息（例如：[1, 2, 3]记录了收藏了该文章的星友ID）
     */
    private Object collected;

    /**
     * 转发信息（例如：[1, 2, 3]记录了转发了该文章的星友ID）
     */
    private Object shared;

    /**
     * 标签信息（例如：[1, 2, 3]记录了标签的ID）
     */
    private Object tags;

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