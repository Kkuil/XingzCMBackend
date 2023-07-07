package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.AccessException;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.mapper.ArticleCommentMapper;
import top.kkuily.xingbackend.model.enums.ArticleCommentReactionEnums;
import top.kkuily.xingbackend.model.po.ArticleComment;
import top.kkuily.xingbackend.model.po.CommentReaction;
import top.kkuily.xingbackend.service.ICommentReactionService;
import top.kkuily.xingbackend.mapper.CommentReactionMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.Token;

import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_KEY_IN_HEADER;
import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_SECRET;

/**
 * @author 小K
 * @description 针对表【comment_reaction】的数据库操作Service实现
 * @createDate 2023-06-16 16:14:53
 */
@Service
public class CommentReactionServiceImpl extends ServiceImpl<CommentReactionMapper, CommentReaction>
        implements ICommentReactionService {

    @Resource
    private ArticleCommentMapper articleCommentMapper;

    @Resource
    private CommentReactionMapper commentReactionMapper;

    /**
     * @param commentId int
     * @param request   HttpServletRequest
     * @return Result
     * @description 评论点赞或取消点赞
     */
    @Override
    public Result like(int commentId, HttpServletRequest request) throws AccessException {
        // 1. 判断评论ID的合法性
        ArticleComment articleComment = articleCommentMapper.selectById(commentId);
        if (articleComment == null) {
            throw new AccessException("评论不存在");
        }

        // 2. 获取用户ID
        // 2.1 解析token
        String token = request.getHeader(USER_TOKEN_KEY_IN_HEADER);
        Claims infoInToken = null;
        try {
            infoInToken = Token.parse(token, USER_TOKEN_SECRET);
        } catch (Exception e) {
            throw new AccessException("令牌解析失败");
        }
        String userId = (String) infoInToken.get("id");

        // 3. 增加记录
        // 3.1 判断是否已经点赞
        QueryWrapper<CommentReaction> commentReactionQueryWrapper = new QueryWrapper<>();
        commentReactionQueryWrapper
                .eq("commentId", commentId)
                .eq("userId", userId)
                .eq("reaction", ArticleCommentReactionEnums.LIKE.getValue())
        ;
        CommentReaction commentReaction = commentReactionMapper.selectOne(commentReactionQueryWrapper);
        if (commentReaction == null) {
            // 判断是否已经点踩
            commentReactionQueryWrapper = new QueryWrapper<>();
            commentReactionQueryWrapper
                    .eq("commentId", commentId)
                    .eq("userId", userId)
                    .eq("reaction", ArticleCommentReactionEnums.DISLIKE.getValue());
            commentReaction = commentReactionMapper.selectOne(commentReactionQueryWrapper);
            // 3.2 如果已经点踩，修改记录
            if (commentReaction != null) {
                commentReaction.setReaction(ArticleCommentReactionEnums.LIKE.getValue());
                int isUpdated = commentReactionMapper.updateById(commentReaction);
                if (isUpdated == 0) {
                    throw new AccessException("点赞失败");
                }
                return Result.success("点赞成功", true, MsgType.NOTIFICATION);
            } else {
                // 3.3 增加记录
                commentReaction = new CommentReaction();
                commentReaction.setCommentId(commentId);
                commentReaction.setUserId(userId);
                commentReaction.setReaction(ArticleCommentReactionEnums.LIKE.getValue());
                int isInserted = commentReactionMapper.insert(commentReaction);
                if (isInserted == 0) {
                    throw new AccessException("点赞失败");
                }
                return Result.success("点赞成功", true, MsgType.NOTIFICATION);
            }

        } else {
            // 3.3 删除记录
            int isDeleted = commentReactionMapper.deleteById(commentId);
            if (isDeleted == 0) {
                throw new AccessException("取消点赞失败");
            }
            return Result.success("取消点赞成功", false, MsgType.NOTIFICATION);
        }
    }

    /**
     * @param commentId int
     * @param request   HttpServletRequest
     * @return Result
     * @description 踩评论或取消踩评论
     */
    @Override
    public Result dislike(int commentId, HttpServletRequest request) throws AccessException {
        // 1. 判断评论ID的合法性
        ArticleComment articleComment = articleCommentMapper.selectById(commentId);
        if (articleComment == null) {
            throw new AccessException("评论不存在");
        }

        // 2. 获取用户ID
        // 2.1 解析token
        String token = request.getHeader(USER_TOKEN_KEY_IN_HEADER);
        Claims infoInToken = null;
        try {
            infoInToken = Token.parse(token, USER_TOKEN_SECRET);
        } catch (Exception e) {
            throw new AccessException("令牌解析失败");
        }
        String userId = (String) infoInToken.get("id");

        // 3. 增加记录
        // 3.1 判断是否已经点赞
        QueryWrapper<CommentReaction> commentReactionQueryWrapper = new QueryWrapper<>();
        commentReactionQueryWrapper
                .eq("commentId", commentId)
                .eq("userId", userId)
                .eq("reaction", ArticleCommentReactionEnums.DISLIKE.getValue())
        ;
        CommentReaction commentReaction = commentReactionMapper.selectOne(commentReactionQueryWrapper);
        if (commentReaction == null) {
            // 判断是否已经点赞
            commentReactionQueryWrapper = new QueryWrapper<>();
            commentReactionQueryWrapper
                    .eq("commentId", commentId)
                    .eq("userId", userId)
                    .eq("reaction", ArticleCommentReactionEnums.LIKE.getValue());
            commentReaction = commentReactionMapper.selectOne(commentReactionQueryWrapper);
            // 3.2 如果已经点赞，修改记录
            if (commentReaction != null) {
                commentReaction.setReaction(ArticleCommentReactionEnums.DISLIKE.getValue());
                int isUpdated = commentReactionMapper.updateById(commentReaction);
                if (isUpdated == 0) {
                    throw new AccessException("点踩失败");
                }
                return Result.success("点踩成功", true, MsgType.NOTIFICATION);
            } else {
                // 3.3 增加记录
                commentReaction = new CommentReaction();
                commentReaction.setCommentId(commentId);
                commentReaction.setUserId(userId);
                commentReaction.setReaction(ArticleCommentReactionEnums.DISLIKE.getValue());
                int isInserted = commentReactionMapper.insert(commentReaction);
                if (isInserted == 0) {
                    throw new AccessException("点踩失败");
                }
                return Result.success("点踩成功", true, MsgType.NOTIFICATION);
            }

        } else {
            // 3.3 删除记录
            int isDeleted = commentReactionMapper.deleteById(commentId);
            if (isDeleted == 0) {
                throw new AccessException("取消点踩失败");
            }
            return Result.success("取消点踩成功", false, MsgType.NOTIFICATION);
        }
    }
}




