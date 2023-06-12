package top.kkuily.xingbackend.web.controller.admin;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.*;
import top.kkuily.xingbackend.model.dto.request.chat_gpt.ChatGPTModelAddBodyDTO;
import top.kkuily.xingbackend.model.dto.request.chat_gpt.ChatGPTModelUpdateBodyDTO;
import top.kkuily.xingbackend.model.po.ChatGPTModel;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.chat_gpt.list.ChatGPTModelListFilterVO;
import top.kkuily.xingbackend.model.vo.chat_gpt.list.ChatGPTModelListParamsVO;
import top.kkuily.xingbackend.model.vo.chat_gpt.list.ChatGPTModelListSortVO;
import top.kkuily.xingbackend.service.IChatGPTModelService;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.ValidateUtils;

import static top.kkuily.xingbackend.constant.commons.Auth.BASE_CHATGPT_MODEL;

/**
 * @author 小K
 * @description ChatGPT相关接口
 */
@RestController
@Slf4j
public class ChatGPTModelController {

    @Resource
    private IChatGPTModelService chatGPTModelService;

    /**
     * @param params String
     * @param sort   String
     * @param filter String
     * @param page   String
     * @return Result
     * @description ChatGPT分页查询接口
     */
    @GetMapping("chatgpt-model")
    public Result getList(String params, String sort, String filter, String page) {
        log.info("page: {}", params);
        ChatGPTModelListParamsVO paramsBean = JSONUtil.toBean(params, ChatGPTModelListParamsVO.class);
        ChatGPTModelListSortVO sortBean = JSONUtil.toBean(sort, ChatGPTModelListSortVO.class);
        ChatGPTModelListFilterVO filterBean = null;
        ListPageVO pageBean = JSONUtil.toBean(page, ListPageVO.class);
        ListParamsVO<ChatGPTModelListParamsVO, ChatGPTModelListSortVO, ChatGPTModelListFilterVO> listParams = new ListParamsVO<>(paramsBean, sortBean, filterBean, pageBean);
        return chatGPTModelService.getList(listParams);
    }

    // region
    // 增删改查

    /**
     * @param chatGPTModelAddBodyDTO ChatGPTModelAddBodyDTO
     * @return Result
     * @description 增
     */
    @PostMapping("chatgpt-model")
    public Result add(@RequestBody ChatGPTModelAddBodyDTO chatGPTModelAddBodyDTO) {
        try {
            //  判空
            ValidateUtils.validateNotEmpty("模型ID", chatGPTModelAddBodyDTO.getId());
            ValidateUtils.validateNotEmpty("模型名", chatGPTModelAddBodyDTO.getName());
            // 判断模型是否存在
            QueryWrapper<ChatGPTModel> chatGPTWrapper = new QueryWrapper<>();
            chatGPTWrapper
                    .eq("id", chatGPTModelAddBodyDTO.getId())
                    .or()
                    .eq("name", chatGPTModelAddBodyDTO.getName());
            ChatGPTModel chatGPTModelInTable = chatGPTModelService.getOne(chatGPTWrapper);
            if (chatGPTModelInTable != null) {
                throw new IllegalArgumentException("模型名已存在，请重新输入");
            }
        } catch (Exception e) {
            return Result.fail(403, e.getMessage(), MsgType.ERROR_MESSAGE);
        }
        ChatGPTModel chatGPT = new ChatGPTModel();
        chatGPTModelAddBodyDTO.convertTo(chatGPT);
        // 保存数据
        boolean isSave = chatGPTModelService.save(chatGPT);
        if (isSave) {
            return Result.success("添加成功", true);
        } else {
            return Result.fail(500, "添加失败", MsgType.ERROR_MESSAGE);
        }
    }

    /**
     * @param id String
     * @return Result
     * @description 删
     */
    @DeleteMapping("chatgpt-model")
    public Result del(String id) {
        // 1. 判断账号是否存在
        ChatGPTModel chatGPTInTable = chatGPTModelService.getById(id);
        if (chatGPTInTable == null) {
            return Result.fail(403, "模型不存在", MsgType.ERROR_MESSAGE);
        }
        // 2. 判断是否是基础ChatGPT
        try {
            if (ArrayUtils.contains(BASE_CHATGPT_MODEL, id)) {
                throw new IllegalArgumentException("不允许删除基础模型");
            }
        } catch (Exception e) {
            return Result.fail(403, "不允许删除基础模型", MsgType.ERROR_MESSAGE);
        }
        boolean isDel = chatGPTModelService.removeById(id);
        if (isDel) {
            return Result.success("删除成功", true);
        } else {
            return Result.fail(403, "删除失败", MsgType.ERROR_MESSAGE);
        }
    }

    /**
     * @param chatGPTUpdateBodyDTO AuthUpdateBodyDTO
     * @return Result
     * @description 改
     */
    @PutMapping("chatgpt-model")
    public Result update(@RequestBody ChatGPTModelUpdateBodyDTO chatGPTUpdateBodyDTO) {
        String id = chatGPTUpdateBodyDTO.getId();
        try {
            ValidateUtils.validateNotEmpty("ChatGPT标识符", id);
        } catch (Exception e) {
            return Result.fail(403, e.getMessage(), MsgType.ERROR_MESSAGE);
        }
        // 判断模型是否存在
        ChatGPTModel chatGPTInTable = chatGPTModelService.getById(id);
        if (chatGPTInTable == null) {
            return Result.fail(403, "ChatGPT不存在", MsgType.ERROR_MESSAGE);
        }
        ChatGPTModel chatGPTModel = null;
        try {
            chatGPTModel = new ChatGPTModel();
        } catch (Exception e) {
            return Result.fail(403, "更新失败", MsgType.ERROR_MESSAGE);
        }
        chatGPTUpdateBodyDTO.convertTo(chatGPTModel);
        boolean isUpdate = chatGPTModelService.updateById(chatGPTModel);
        if (isUpdate) {
            return Result.success("更新成功", true);
        } else {
            return Result.fail(403, "更新失败", MsgType.ERROR_MESSAGE);
        }
    }

    /**
     * @param id String
     * @return Result
     * @description 获取某个ChatGPT
     */
    @GetMapping("/chatgpt-model/:id")
    public Result get(@PathParam("id") String id) {
        // 判断账号是否存在
        ChatGPTModel chatGPTInTable = chatGPTModelService.getById(id);
        if (chatGPTInTable == null) {
            return Result.fail(403, "ChatGPT不存在", MsgType.ERROR_MESSAGE);
        }
        return Result.success("获取成功", true);
    }

    // endregion
}
