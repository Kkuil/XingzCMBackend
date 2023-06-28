package top.kkuily.xingbackend.web.controller.user;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kkuily.xingbackend.anotation.ApiSignAuth;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.model.dto.response.ListResDTO;
import top.kkuily.xingbackend.model.po.Category;
import top.kkuily.xingbackend.service.ICategoryService;
import top.kkuily.xingbackend.utils.Result;

import java.util.List;

/**
 * @author 小K
 * @description 文章分类相关接口
 */
@RestController
@Slf4j
public class UArticleCategoryController {

    @Resource
    private ICategoryService articleCategoryService;

    /**
     * @return Result
     * @description 文章分类分页查询接口
     */
    @GetMapping("uarticle-category")
    @ApiSignAuth
    public Result getList() {
        List<Category> list = articleCategoryService.list();
        ListResDTO<Category> articleCategoryListResDTO = new ListResDTO<>();
        articleCategoryListResDTO.setCurrent(1);
        articleCategoryListResDTO.setPageSize(list.size());
        articleCategoryListResDTO.setList(list);
        articleCategoryListResDTO.setTotal(list.size());
        return Result.success("获取成功", articleCategoryListResDTO, MsgType.SILENT);
    }
}
