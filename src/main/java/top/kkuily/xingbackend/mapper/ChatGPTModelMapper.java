package top.kkuily.xingbackend.mapper;

import org.apache.ibatis.annotations.Param;
import top.kkuily.xingbackend.model.dto.response.chat_gpt.ChatGPTModelInfoResDTO;
import top.kkuily.xingbackend.model.po.ChatGPTModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.chat_gpt.list.ChatGPTModelListFilterVO;
import top.kkuily.xingbackend.model.vo.chat_gpt.list.ChatGPTModelListParamsVO;
import top.kkuily.xingbackend.model.vo.chat_gpt.list.ChatGPTModelListSortVO;

import java.util.List;

/**
 * @author 小K
 * @description 针对表【chatgpt_model】的数据库操作Mapper
 * @createDate 2023-06-11 09:51:46
 * @Entity top.kkuily.xingbackend.model.po.ChatGPTModel
 */
public interface ChatGPTModelMapper extends BaseMapper<ChatGPTModel> {
    /**
     * @param params ChatGPTModelListParamsVO
     * @param sort   ChatGPTModelListSortVO
     * @param filter ChatGPTModelListFilterVO
     * @param page   ListPageVO
     * @return List<ChatGPTModelInfoResDTO>
     * @description 带有limit的分页查询
     */
    List<ChatGPTModelInfoResDTO> listChatGPTModelsWithLimit(
            @Param("params") ChatGPTModelListParamsVO params,
            @Param("sort") ChatGPTModelListSortVO sort,
            @Param("filter") ChatGPTModelListFilterVO filter,
            @Param("page") ListPageVO page
    );

    /**
     * @param params ChatGPTModelListParamsVO
     * @param sort   ChatGPTModelListSortVO
     * @param filter ChatGPTModelListFilterVO
     * @param page   ListPageVO
     * @return List<ChatGPTModelInfoResDTO>
     * @description 不带有limit的分页查询
     */
    Integer listChatGPTModelsWithNotLimit(
            @Param("params") ChatGPTModelListParamsVO params,
            @Param("sort") ChatGPTModelListSortVO sort,
            @Param("filter") ChatGPTModelListFilterVO filter,
            @Param("page") ListPageVO page
    );

}




