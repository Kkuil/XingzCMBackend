package top.kkuily.xingbackend.model.dto.response.chat_gpt;

import lombok.Data;

import java.util.Date;

/**
 * @author 小K
 * @description ChatGPT模型分页查询返回类
 */
@Data
public class ChatGPTModelInfoResDTO {
    /**
     * ChatGPT模型ID
     */
    private String id;

    /**
     * 模型名
     */
    private String name;

    /**
     * 封面图
     */
    private String cover;

    /**
     * 是否逻辑删除
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
