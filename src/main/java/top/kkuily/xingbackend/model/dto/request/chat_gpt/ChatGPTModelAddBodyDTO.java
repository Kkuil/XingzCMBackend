package top.kkuily.xingbackend.model.dto.request.chat_gpt;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.kkuily.xingbackend.model.po.ChatGPTModel;

/**
 * @author 小K
 * @description 增加ChatGPT模型的DTO类
 */
@Data
public class ChatGPTModelAddBodyDTO {

    /**
     * 模型ID
     */
    private String id;

    /**
     * 模型名
     */
    private String name;

    /**
     * 模型封面
     */
    private String cover;

    /**
     * 转换为本类方法
     *
     * @param chatGPT ChatGPTModel
     */
    public void convertTo(ChatGPTModel chatGPT) {
        BeanUtils.copyProperties(this, chatGPT);
    }
}
