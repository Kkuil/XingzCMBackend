package top.kkuily.xingbackend.web.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kkuily.xingbackend.model.po.Tag;
import top.kkuily.xingbackend.service.ITagService;
import top.kkuily.xingbackend.utils.Result;

import java.util.List;

/**
 * @description 角色相关接口
 * @author 小K
 */
@RestController
public class TagController {

    @Resource
    private ITagService tagService;

    /**
     * @description 获取全部标签
     * @return Result
     */
    @GetMapping("tag")
    public Result listTags() {
        List<Tag> list = tagService.list();
        return Result.success("获取成功", list);
    }
}
