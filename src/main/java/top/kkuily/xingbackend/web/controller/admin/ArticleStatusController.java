package top.kkuily.xingbackend.web.controller.admin;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kkuily.xingbackend.anotation.AdminAuthToken;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.model.dto.response.ListResDTO;
import top.kkuily.xingbackend.model.po.ArticleStatus;
import top.kkuily.xingbackend.service.IArticleStatusService;
import top.kkuily.xingbackend.utils.Result;

import java.util.List;

/**
 * @author 小K
 * @description 文章状态相关接口
 */
@RestController
@Slf4j
public class ArticleStatusController {

    @Resource
    private IArticleStatusService articleStatusService;

    /**
     * @return Result
     * @description 文章状态分页查询接口
     */
    @GetMapping("article-status")
    @AdminAuthToken
    public Result getList() {
        List<ArticleStatus> list = articleStatusService.list();
        ListResDTO<ArticleStatus> articleStatusListResDTO = new ListResDTO<>();
        articleStatusListResDTO.setCurrent(1);
        articleStatusListResDTO.setPageSize(10);
        articleStatusListResDTO.setList(list);
        articleStatusListResDTO.setTotal(10);
        return Result.success("获取成功", articleStatusListResDTO, MsgType.NOTIFICATION);
    }
}
