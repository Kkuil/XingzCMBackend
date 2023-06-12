package top.kkuily.xingbackend.model.vo.chat_gpt.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 小K
 * @description ChatGPTModel表分页查询排序参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGPTModelListSortVO {
    private String createdTime;
    private String modifiedTime;
}
