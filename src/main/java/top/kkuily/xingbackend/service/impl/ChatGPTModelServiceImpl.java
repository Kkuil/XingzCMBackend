package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import top.kkuily.xingbackend.model.dto.response.ListResDTO;
import top.kkuily.xingbackend.model.dto.response.admin.AdminInfoResDTO;
import top.kkuily.xingbackend.model.dto.response.chat_gpt.ChatGPTModelInfoResDTO;
import top.kkuily.xingbackend.model.po.ChatGPTModel;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.chat_gpt.list.ChatGPTModelListFilterVO;
import top.kkuily.xingbackend.model.vo.chat_gpt.list.ChatGPTModelListParamsVO;
import top.kkuily.xingbackend.model.vo.chat_gpt.list.ChatGPTModelListSortVO;
import top.kkuily.xingbackend.service.IChatGPTModelService;
import top.kkuily.xingbackend.mapper.ChatGPTModelMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.utils.Result;

import java.util.List;

/**
 * @author 小K
 * @description 针对表【chatgpt】的数据库操作Service实现
 * @createDate 2023-06-11 09:51:46
 */
@Service
public class ChatGPTModelServiceImpl extends ServiceImpl<ChatGPTModelMapper, ChatGPTModel>
        implements IChatGPTModelService {

    @Resource
    private ChatGPTModelMapper chatGPTModelMapper;

    @Override
    public Result getList(ListParamsVO<ChatGPTModelListParamsVO, ChatGPTModelListSortVO, ChatGPTModelListFilterVO> chatGPTModelListParams) {
        // 获取数据
        ChatGPTModelListParamsVO params = chatGPTModelListParams.getParams();
        ChatGPTModelListSortVO sort = chatGPTModelListParams.getSort();
        ChatGPTModelListFilterVO filter = chatGPTModelListParams.getFilter();
        ListPageVO page = chatGPTModelListParams.getPage();

        // 设置默认值
        if (page.getCurrent() <= 0) {
            page.setCurrent(1);
        }
        if (page.getPageSize() <= 0) {
            page.setPageSize(10);
        }

        // 分页数据处理
        ListPageVO listPageVO = new ListPageVO();
        listPageVO.setCurrent((page.getCurrent() - 1) * page.getPageSize());
        listPageVO.setPageSize(page.getPageSize());
        chatGPTModelListParams.setPage(listPageVO);

        // 查询数据
        List<ChatGPTModelInfoResDTO> chatGPTModelInfoResDTOS = null;
        // 总条数
        int total = 0;
        try {
            chatGPTModelInfoResDTOS = chatGPTModelMapper.listChatGPTModelsWithLimit(params, sort, filter, listPageVO);
            total = chatGPTModelMapper.listChatGPTModelsWithNotLimit(params, sort, filter, listPageVO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 封装数据
        ListResDTO<ChatGPTModelInfoResDTO> chatGPTModelListRes = new ListResDTO<>();
        chatGPTModelListRes.setCurrent(page.getCurrent());
        chatGPTModelListRes.setPageSize(page.getPageSize());
        chatGPTModelListRes.setList(chatGPTModelInfoResDTOS);
        chatGPTModelListRes.setTotal(total);

        return Result.success("获取成功", chatGPTModelListRes);
    }
}




