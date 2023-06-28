package top.kkuily.xingbackend.web.controller.user;

import jakarta.annotation.Resource;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.model.dto.response.ListResDTO;
import top.kkuily.xingbackend.model.enums.IsDeletedEnums;
import top.kkuily.xingbackend.model.po.Department;
import top.kkuily.xingbackend.model.po.Tag;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.service.ITagService;
import top.kkuily.xingbackend.utils.Result;

import java.util.List;

/**
 * @author 小K
 * @description 标签相关接口
 */
@RestController
@Slf4j
public class UTagController {

    @Resource
    private ITagService tagService;

    /**
     * @return Result
     * @description 标签分页查询接口
     */
    @GetMapping("utag")
    public Result getList() {
        List<Tag> list = tagService.list();
        ListResDTO<Tag> deptListRes = new ListResDTO<>();
        deptListRes.setCurrent(1);
        deptListRes.setPageSize(list.size());
        deptListRes.setList(list);
        deptListRes.setTotal(list.size());
        return Result.success("获取成功", deptListRes, MsgType.SILENT);
    }

    /**
     * @param id String
     * @return Result
     * @description 获取某个标签
     */
    @GetMapping("/utag/:id")
    public Result get(@PathParam("id") String id) {
        // 1. 判断账号是否存在
        Tag tagInTable = tagService.getById(id);
        if (tagInTable == null) {
            return Result.fail(403, "标签不存在", MsgType.ERROR_MESSAGE);
        }
        // 2. 判断是否被逻辑删除
        String isDeleted = tagInTable.getIsDeleted();
        if (IsDeletedEnums.NO.getValue().equals(isDeleted)) {
            return Result.success("获取成功", true);
        } else {
            return Result.success("获取成功，该标签已被删除", true);
        }
    }
}
