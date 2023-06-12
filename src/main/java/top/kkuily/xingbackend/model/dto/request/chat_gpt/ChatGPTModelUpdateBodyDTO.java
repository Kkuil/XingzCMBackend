package top.kkuily.xingbackend.model.dto.request.chat_gpt;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.kkuily.xingbackend.model.po.ChatGPTModel;
import top.kkuily.xingbackend.model.po.Role;

/**
 * @author 小K
 * @description 更新ChatGPT模型的传输数据
 */
@Data
public class ChatGPTModelUpdateBodyDTO {

    /**
     * 模型ID
     */
    private String id;

    /**
     * ChatGPT模型名
     */
    private String name;

    /**
     * 封面
     */
    private String cover;

    /**
     * @param chatGPTModel ChatGPTModel
     */
    public void convertTo(ChatGPTModel chatGPTModel) {
        BeanUtils.copyProperties(this, chatGPTModel);
    }
}
