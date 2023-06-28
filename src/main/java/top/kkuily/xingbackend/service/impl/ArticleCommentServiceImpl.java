package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleCommentParamsDTO;
import top.kkuily.xingbackend.model.po.ArticleComment;
import top.kkuily.xingbackend.service.IArticleCommentService;
import top.kkuily.xingbackend.mapper.ArticleCommentMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.Token;

import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_KEY_IN_HEADER;
import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_SECRET;

/**
 * @author 小K
 * @description 针对表【article_comment】的数据库操作Service实现
 * @createDate 2023-06-16 16:14:06
 */
@Service
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment>
        implements IArticleCommentService {

    /**
     * @param uArticleCommentParamsDTO UArticleCommentParamsDTO
     * @param request                  HttpServletRequest
     * @return Result
     * @description 用户评论
     */
    @Override
    public Result comment(UArticleCommentParamsDTO uArticleCommentParamsDTO, HttpServletRequest request) {
        // 1. 获取用户ID
        String token = request.getHeader(USER_TOKEN_KEY_IN_HEADER);
        Claims parseToken = Token.parse(token, USER_TOKEN_SECRET);
        String userId = (String) parseToken.get("id");

        // 2. 保存数据
        ArticleComment articleComment = new ArticleComment();
        articleComment.setArticleId(uArticleCommentParamsDTO.getArticleId());
        articleComment.setParentId(uArticleCommentParamsDTO.getParentId());
        articleComment.setUserId(userId);
        articleComment.setContent(uArticleCommentParamsDTO.getContent());

        boolean isComment = this.save(articleComment);
        if (isComment) {
            return Result.success("评论成功", true, MsgType.NOTIFICATION);
        } else {
            return Result.fail(401, "评论失败", MsgType.ERROR_MESSAGE);
        }
    }
}




