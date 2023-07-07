package top.kkuily.xingbackend.web.controller.user;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.AccessException;
import org.springframework.web.bind.annotation.*;
import top.kkuily.xingbackend.anotation.ApiSignAuth;
import top.kkuily.xingbackend.anotation.FrequencyControl;
import top.kkuily.xingbackend.anotation.UserAuthToken;
import top.kkuily.xingbackend.model.dto.request.activity.UActivityListParamsDTO;
import top.kkuily.xingbackend.model.dto.request.article.admin.ArticleAddBodyDTO;
import top.kkuily.xingbackend.model.dto.request.article.admin.ArticleUpdateBodyDTO;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleCommentAddParamsDTO;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleListParamsDTO;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleListParamsWithUserIdDTO;
import top.kkuily.xingbackend.model.po.Activity;
import top.kkuily.xingbackend.service.IActivityService;
import top.kkuily.xingbackend.service.IArticleCommentService;
import top.kkuily.xingbackend.service.IArticleService;
import top.kkuily.xingbackend.utils.Result;

/**
 * @author 小K
 * @description 活动相关接口
 */
@RestController
@Slf4j
public class UActivityController {

    @Resource
    private IActivityService activityService;

    /**
     * @param uActivityListParamsDTO UActivityListParamsDTO
     * @param request            HttpServletRequest
     * @return Result
     * @description 文章分页查询接口
     */
    @GetMapping("uactivity")
    @FrequencyControl(time = 120, count = 20, target = FrequencyControl.Target.IP)
    public Result list(UActivityListParamsDTO uActivityListParamsDTO, HttpServletRequest request) {
        return activityService.userGetList(uActivityListParamsDTO, request);
    }

    /**
     * @param id String
     * @return Result
     * @description 通过活动ID获取活动
     */
    @GetMapping("uactivity/{activityId}")
    public Result get(@PathVariable("activityId") String id) {
        Activity activity = activityService.getById(id);
        return Result.success("获取成功", activity);
    }
}
