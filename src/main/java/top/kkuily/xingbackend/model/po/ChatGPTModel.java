package top.kkuily.xingbackend.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName chatgpt_model
 */
@TableName(value = "chatgpt_model")
@Data
public class ChatGPTModel implements Serializable {
    /**
     * 模型ID
     */
    @TableId
    private String id;

    /**
     * 模型名称
     */
    private String name;

    /**
     * 模型封面图
     */
    private String cover;

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