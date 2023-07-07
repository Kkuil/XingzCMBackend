package top.kkuily.xingbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.expression.AccessException;
import top.kkuily.xingbackend.model.po.CommentReaction;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kkuily.xingbackend.utils.Result;

/**
 * @author 小K
 * @description 针对表【comment_reaction】的数据库操作Service
 * @createDate 2023-06-16 16:14:53
 */
public interface ICommentReactionService extends IService<CommentReaction> {

    /**
     * @param commentId String
     * @param request   HttpServletRequest
     * @return Result
     * @description 评论点赞或取消点赞
     */
    Result like(int commentId, HttpServletRequest request) throws AccessException;

    /**
     * @param commentId String
     * @param request   HttpServletRequest
     * @return Result
     * @description 评论点赞或取消点赞
     */
    Result dislike(int commentId, HttpServletRequest request) throws AccessException;
}
