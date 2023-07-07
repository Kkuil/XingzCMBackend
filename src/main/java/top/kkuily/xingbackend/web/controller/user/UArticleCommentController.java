package top.kkuily.xingbackend.web.controller.user;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.AccessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kkuily.xingbackend.anotation.ApiSignAuth;
import top.kkuily.xingbackend.anotation.FrequencyControl;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleCommentListParamsDTO;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleSubCommentListParamsDTO;
import top.kkuily.xingbackend.service.IArticleCommentService;
import top.kkuily.xingbackend.utils.Result;

@RestController
@Slf4j(topic = "article-comment")
public class UArticleCommentController {

    @Resource
    private IArticleCommentService articleCommentService;

    /**
     * @param uArticleCommentListParamsDTO UArticleCommentListParamsDTO
     * @param request                      HttpServletRequest
     * @return Result
     * @throws AccessException Exception
     */
    @GetMapping("uarticle-comment")
    @ApiSignAuth
    @FrequencyControl(time = 120, count = 60, target = FrequencyControl.Target.IP)
    public Result list(UArticleCommentListParamsDTO uArticleCommentListParamsDTO, HttpServletRequest request) throws AccessException {
        return articleCommentService.getList(uArticleCommentListParamsDTO, request);
    }

    /**
     * @param uArticleSubCommentListParamsDTO UArticleSubCommentListParamsDTO
     * @param request                         HttpServletRequest
     * @return Result
     * @description 获取评论的子评论
     */
    @GetMapping("uarticle-comment/sub-comment")
    @FrequencyControl(time = 120, count = 60, target = FrequencyControl.Target.IP)
    public Result getSubCommentById(UArticleSubCommentListParamsDTO uArticleSubCommentListParamsDTO, HttpServletRequest request) {
        return articleCommentService.getSubCommentById(uArticleSubCommentListParamsDTO, request);
    }
}
