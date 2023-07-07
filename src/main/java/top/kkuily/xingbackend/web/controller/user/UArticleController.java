package top.kkuily.xingbackend.web.controller.user;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.AccessException;
import org.springframework.web.bind.annotation.*;
import top.kkuily.xingbackend.anotation.ApiSignAuth;
import top.kkuily.xingbackend.anotation.FrequencyControl;
import top.kkuily.xingbackend.anotation.UserAuthToken;
import top.kkuily.xingbackend.model.dto.request.article.admin.ArticleAddBodyDTO;
import top.kkuily.xingbackend.model.dto.request.article.admin.ArticleUpdateBodyDTO;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleCommentAddParamsDTO;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleListParamsDTO;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleListParamsWithUserIdDTO;
import top.kkuily.xingbackend.service.IArticleCommentService;
import top.kkuily.xingbackend.service.IArticleService;
import top.kkuily.xingbackend.utils.Result;

/**
 * @author 小K
 * @description 文章相关接口
 */
@RestController
@Slf4j
public class UArticleController {

    @Resource
    private IArticleService articleService;

    @Resource
    private IArticleCommentService articleCommentService;

    /**
     * @param uArticleListParams UArticleListParamsDTO
     * @param request            HttpServletRequest
     * @return Result
     * @description 文章分页查询接口
     */
    @GetMapping("uarticle")
    @ApiSignAuth
    @FrequencyControl(time = 120, count = 20, target = FrequencyControl.Target.IP)
    public Result list(UArticleListParamsDTO uArticleListParams, HttpServletRequest request) {
        return articleService.userGetList(uArticleListParams, request);
    }

    /**
     * @param uArticleListParamsWithUserIdDTO UArticleListParamsWithUserIdDTO
     * @param request                         HttpServletRequest
     * @return Result
     * @description 文章分页查询接口
     */
    @GetMapping("uarticle-user-id")
    @ApiSignAuth
    public Result listWithUserId(UArticleListParamsWithUserIdDTO uArticleListParamsWithUserIdDTO, HttpServletRequest request) {
        return articleService.listWithUserId(uArticleListParamsWithUserIdDTO, request);
    }

    // region
    // 增删改查

    /**
     * @param articleAddBodyDTO AuthAddBodyDTO
     * @return Result
     * @description 发布
     */
    @PostMapping("uarticle")
    @UserAuthToken
    @ApiSignAuth
    public Result publish(@RequestBody ArticleAddBodyDTO articleAddBodyDTO, HttpServletRequest request) {
        return articleService.publish(articleAddBodyDTO, request);
    }

    /**
     * @param id String
     * @return Result
     * @description 删
     */
    @DeleteMapping("uarticle")
    @ApiSignAuth
    public Result del(String id) {
        return null;
    }

    /**
     * @param articleUpdateBodyDTO AuthUpdateBodyDTO
     * @return Result
     * @description 改
     */
    @PutMapping("uarticle")
    @ApiSignAuth
    public Result update(@RequestBody ArticleUpdateBodyDTO articleUpdateBodyDTO) {
        return Result.success("更新成功", true);
    }

    /**
     * @param articleId String
     * @param request   HttpServletRequest
     * @return Result
     * @description 用户获取文章详情
     */
    @GetMapping("/uarticle/{articleId}")
    @ApiSignAuth
    public Result getArticleAndUserInfoByIdWithUser(@PathVariable String articleId, HttpServletRequest request) throws AccessException {
        return articleService.getArticleDetail(articleId, request);
    }
    // endregion

    /**
     * @return
     */
    @PostMapping("uarticle/draft")
    @ApiSignAuth
    @UserAuthToken
    public Result saveDraft(@RequestBody ArticleAddBodyDTO articleAddBodyDTO, HttpServletRequest request) {
        return articleService.saveDraft(articleAddBodyDTO, request);
    }

    /**
     * @param articleId String
     * @param request   HttpServletRequest
     * @return Result
     * @description 点赞获取消点赞
     */
    @GetMapping("uarticle/like/{articleId}")
    @ApiSignAuth
    @UserAuthToken
    @FrequencyControl(time = 120, count = 60, target = FrequencyControl.Target.IP)
    @FrequencyControl(time = 60, count = 20, target = FrequencyControl.Target.UID)
    public Result like(@PathVariable("articleId") String articleId, HttpServletRequest request) {
        return articleService.like(articleId, request);
    }

    /**
     * @param articleId String
     * @param request   HttpServletRequest
     * @return Result
     * @description 收藏获取消收藏
     */
    @GetMapping("uarticle/collect/{articleId}")
    @ApiSignAuth
    @UserAuthToken
    @FrequencyControl(time = 120, count = 60, target = FrequencyControl.Target.IP)
    @FrequencyControl(time = 60, count = 20, target = FrequencyControl.Target.UID)
    public Result collect(@PathVariable("articleId") String articleId, HttpServletRequest request) {
        return articleService.collect(articleId, request);
    }

    /**
     * @param articleId String
     * @param request   HttpServletRequest
     * @return Result
     * @description 浏览过
     */
    @GetMapping("uarticle/visit/{articleId}")
    @ApiSignAuth
    @UserAuthToken
    public Result visit(@PathVariable("articleId") String articleId, HttpServletRequest request) {
        return articleService.visit(articleId, request);
    }

    /**
     * @param userId String
     * @return Result
     * @description 获取浏览过的文章
     */
    @GetMapping("uarticle/visited")
    @ApiSignAuth
    public Result listVisited(String userId, int current, int pageSize) {
        return articleService.listVisited(userId, current, pageSize);
    }

    /**
     * @param uArticleCommentParamsDTO UArticleCommentAddParamsDTO
     * @return Result
     * @description 用户评论接口
     */
    @PostMapping("uarticle/comment")
    @UserAuthToken
    @ApiSignAuth
    @FrequencyControl(time = 120, count = 20, target = FrequencyControl.Target.IP)
    @FrequencyControl(time = 60, count = 5, target = FrequencyControl.Target.UID)
    public Result comment(UArticleCommentAddParamsDTO uArticleCommentParamsDTO, HttpServletRequest request) {
        return articleCommentService.comment(uArticleCommentParamsDTO, request);
    }
}
