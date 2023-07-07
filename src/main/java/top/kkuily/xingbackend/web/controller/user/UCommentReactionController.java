package top.kkuily.xingbackend.web.controller.user;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.AccessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kkuily.xingbackend.anotation.ApiSignAuth;
import top.kkuily.xingbackend.anotation.UserAuthToken;
import top.kkuily.xingbackend.service.ICommentReactionService;
import top.kkuily.xingbackend.utils.Result;

@RestController
@Slf4j(topic = "comment-reaction")
public class UCommentReactionController {

    @Resource
    private ICommentReactionService commentReactionService;

    /**
     * @param commentId String
     * @param request   HttpServletRequest
     * @return Result
     * @description 评论点赞或取消点赞
     */
    @GetMapping("comment-reaction/like")
    @UserAuthToken
    @ApiSignAuth
    public Result like(int commentId, HttpServletRequest request) throws AccessException {
        return commentReactionService.like(commentId, request);
    }

    /**
     * @param commentId String
     * @param request   HttpServletRequest
     * @return Result
     * @description 评论点赞或取消点赞
     */
    @GetMapping("comment-reaction/dislike")
    @UserAuthToken
    @ApiSignAuth
    public Result dislike(int commentId, HttpServletRequest request) throws AccessException {
        return commentReactionService.dislike(commentId, request);
    }
}
