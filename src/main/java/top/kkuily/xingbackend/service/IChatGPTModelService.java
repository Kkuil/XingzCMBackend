package top.kkuily.xingbackend.service;

import top.kkuily.xingbackend.model.po.ChatGPTModel;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.chat_gpt.list.ChatGPTModelListFilterVO;
import top.kkuily.xingbackend.model.vo.chat_gpt.list.ChatGPTModelListParamsVO;
import top.kkuily.xingbackend.model.vo.chat_gpt.list.ChatGPTModelListSortVO;
import top.kkuily.xingbackend.utils.Result;

/**
* @author 小K
* @description 针对表【chatgpt】的数据库操作Service
* @createDate 2023-06-11 09:51:46
*/
public interface IChatGPTModelService extends IService<ChatGPTModel> {

    Result getList(ListParamsVO<ChatGPTModelListParamsVO, ChatGPTModelListSortVO, ChatGPTModelListFilterVO> listParams);
}
