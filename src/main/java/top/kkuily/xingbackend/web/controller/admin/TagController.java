package top.kkuily.xingbackend.web.controller.admin;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.kkuily.xingbackend.model.dto.request.tag.TagAddBodyDTO;
import top.kkuily.xingbackend.model.dto.request.tag.TagUpdateBodyDTO;
import top.kkuily.xingbackend.model.enums.IsDeletedEnums;
import top.kkuily.xingbackend.model.po.Tag;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.tag.list.TagListFilterVO;
import top.kkuily.xingbackend.model.vo.tag.list.TagListParamsVO;
import top.kkuily.xingbackend.model.vo.tag.list.TagListSortVO;
import top.kkuily.xingbackend.service.ITagService;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.ValidateUtils;

/**
 * @author 小K
 * @description 标签相关接口
 */
@RestController
@Slf4j
public class TagController {

    @Resource
    private ITagService tagService;

    /**
     * @param params ArticleListParamsVO
     * @param sort   ArticleListSortVO
     * @param filter ArticleListFilterVO
     * @param page   TagListPageVO
     * @return Result
     * @description 标签分页查询接口
     */
    @GetMapping("tag")
    public Result getList(String params, String sort, String filter, String page) {
        log.info("page: {}", params);
        TagListParamsVO paramsBean = JSONUtil.toBean(params, TagListParamsVO.class);
        TagListSortVO sortBean = JSONUtil.toBean(sort, TagListSortVO.class);
        TagListFilterVO filterBean = JSONUtil.toBean(filter, TagListFilterVO.class);
        ListPageVO pageBean = JSONUtil.toBean(page, ListPageVO.class);
        ListParamsVO<TagListParamsVO, TagListSortVO, TagListFilterVO> listParams = new ListParamsVO<>(paramsBean, sortBean, filterBean, pageBean);
        return tagService.getList(listParams);
    }

    // region
    // 增删改查

    /**
     * @param tagAddBodyDTO AuthAddBodyDTO
     * @return Result
     * @description 增
     */
    @PostMapping("tag")
    public Result add(@RequestBody TagAddBodyDTO tagAddBodyDTO) {
        try {
            // 1. 判空
            ValidateUtils.validateNotEmpty("标签名", tagAddBodyDTO.getTagName());
            // 2. 判长
            if (!StringUtils.isEmpty(tagAddBodyDTO.getTagName())) {
                ValidateUtils.validateLength("标签名", tagAddBodyDTO.getTagName(), 2, 10);
            }
            // 判断标签名是否存在
            QueryWrapper<Tag> tagWrapper = new QueryWrapper<>();
            tagWrapper.eq("tagName", tagAddBodyDTO.getTagName());
            Tag tagInTable = tagService.getOne(tagWrapper);
            if (tagInTable != null) {
                throw new IllegalArgumentException("标签名已存在，请重新输入");
            }
        } catch (Exception e) {
            return Result.fail(403, e.getMessage(), MsgType.ERROR_MESSAGE);
        }
        Tag tag = new Tag();
        tagAddBodyDTO.convertTo(tag);
        // 保存数据
        boolean isSave = tagService.save(tag);
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
    @DeleteMapping("tag")
    public Result del(String id) {
        // 1. 判断id是否存在
        Tag tagInTable = tagService.getById(id);
        if (tagInTable == null) {
            return Result.fail(403, "标签不存在", MsgType.ERROR_MESSAGE);
        }
        // 2. 判断是否删除
        boolean isDel = tagService.removeById(id);
        if (isDel) {
            return Result.success("删除成功", true);
        } else {
            return Result.fail(403, "删除失败", MsgType.ERROR_MESSAGE);
        }
    }

    /**
     * @param tagUpdateBodyDTO AuthUpdateBodyDTO
     * @return Result
     * @description 改
     */
    @PutMapping("tag")
    public Result update(@RequestBody TagUpdateBodyDTO tagUpdateBodyDTO) {
        log.info("tagUpdateBodyDTO: {}", tagUpdateBodyDTO);
        String id = tagUpdateBodyDTO.getId();
        try {
            ValidateUtils.validateNotEmpty("标签标识符", id);
            // 判断标签是否存在
            Tag tagInTable = tagService.getById(id);
            if (tagInTable == null) {
                return Result.fail(403, "标签不存在", MsgType.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            return Result.fail(403, e.getMessage(), MsgType.ERROR_MESSAGE);
        }
        Tag tag = new Tag();
        tagUpdateBodyDTO.convertTo(tag);
        // 判断是否删除
        boolean isDel = tagService.updateById(tag);
        if (isDel) {
            return Result.success("更新成功", true);
        } else {
            return Result.fail(403, "更新失败", MsgType.ERROR_MESSAGE);
        }
    }

    /**
     * @param id String
     * @return Result
     * @description 获取某个标签
     */
    @GetMapping("/tag/:id")
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

    // endregion
}
