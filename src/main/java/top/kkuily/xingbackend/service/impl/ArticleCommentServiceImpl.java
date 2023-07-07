package top.kkuily.xingbackend.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.AccessException;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleCommentAddParamsDTO;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleCommentListParamsDTO;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleSubCommentListParamsDTO;
import top.kkuily.xingbackend.model.dto.response.ListResDTO;
import top.kkuily.xingbackend.model.po.ArticleComment;
import top.kkuily.xingbackend.model.vo.article.detail.CommentDetailInfoVO;
import top.kkuily.xingbackend.model.vo.article.detail.SubCommentDetailInfoVO;
import top.kkuily.xingbackend.service.IArticleCommentService;
import top.kkuily.xingbackend.mapper.ArticleCommentMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.Token;

import java.util.List;
import java.util.Objects;

import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_KEY_IN_HEADER;
import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_SECRET;

/**
 * @author 小K
 * @description 针对表【article_comment】的数据库操作Service实现
 * @createDate 2023-06-16 16:14:06
 */
@Service
@Slf4j(topic = "article.comment")
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment>
        implements IArticleCommentService {

    @Resource
    private ArticleCommentMapper articleCommentMapper;

    /**
     * @param uArticleCommentParamsDTO UArticleCommentAddParamsDTO
     * @param request                  HttpServletRequest
     * @return Result
     * @description 用户评论
     */
    @Override
    public Result comment(UArticleCommentAddParamsDTO uArticleCommentParamsDTO, HttpServletRequest request) {
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

    /**
     * @param uArticleCommentListParamsDTO UArticleCommentListParamsDTO
     * @param request                      HttpServletRequest
     * @return Result
     * @throws AccessException
     * @description 获取评论列表
     */
    @Override
    public Result getList(UArticleCommentListParamsDTO uArticleCommentListParamsDTO, HttpServletRequest request) throws AccessException {
        String token = request.getHeader(USER_TOKEN_KEY_IN_HEADER);

        // 获取token
        Claims userInfoInToken = null;
        try {
            userInfoInToken = Token.parse(token, USER_TOKEN_SECRET);
        } catch (Exception e) {
            log.error("令牌解析失败");
        }

        // 获取评论
        List<CommentDetailInfoVO> articleCommentInfoResWithUserDTOS = null;
        int total = 0;
        try {
            articleCommentInfoResWithUserDTOS = articleCommentMapper.listArticleCommentWithLimit(
                    uArticleCommentListParamsDTO.getArticleId(),
                    !ObjectUtil.isEmpty(userInfoInToken) ? Objects.requireNonNull(userInfoInToken).get("id").toString() : "",
                    uArticleCommentListParamsDTO.getCurrent(),
                    uArticleCommentListParamsDTO.getPageSize()
            );
            total = articleCommentMapper.listArticleCommentWithNotLimit(
                    uArticleCommentListParamsDTO.getArticleId()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 封装数据
        ListResDTO<CommentDetailInfoVO> articleListRes = new ListResDTO<>();
        if (articleCommentInfoResWithUserDTOS != null) {
            articleListRes.setCurrent(uArticleCommentListParamsDTO.getCurrent());
            articleListRes.setPageSize(uArticleCommentListParamsDTO.getPageSize());
            articleListRes.setList(articleCommentInfoResWithUserDTOS);
            articleListRes.setTotal(total);
        }

        return Result.success("获取成功", articleListRes);
    }

    /**
     * @param uArticleSubCommentListParamsDTO UArticleSubCommentListParamsDTO
     * @param request                         HttpServletRequest
     * @return Result
     * @description 通过评论ID获取子评论
     */
    @Override
    public Result getSubCommentById(UArticleSubCommentListParamsDTO uArticleSubCommentListParamsDTO, HttpServletRequest request) {
        String token = request.getHeader(USER_TOKEN_KEY_IN_HEADER);

        // 获取token
        Claims userInfoInToken = null;
        try {
            userInfoInToken = Token.parse(token, USER_TOKEN_SECRET);
        } catch (Exception e) {
            log.error("令牌解析失败");
        }

        // 获取评论
        List<SubCommentDetailInfoVO> articleCommentInfoResWithUserDTOS = null;
        int total = 0;
        try {
            articleCommentInfoResWithUserDTOS = articleCommentMapper.listArticleSubCommentWithLimit(
                    uArticleSubCommentListParamsDTO.getCommentId(),
                    !ObjectUtil.isEmpty(userInfoInToken) ? Objects.requireNonNull(userInfoInToken).get("id").toString() : "",
                    uArticleSubCommentListParamsDTO.getCurrent(),
                    uArticleSubCommentListParamsDTO.getPageSize()
            );
            total = articleCommentMapper.listArticleSubCommentWithNotLimit(
                    uArticleSubCommentListParamsDTO.getCommentId()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 封装数据
        ListResDTO<SubCommentDetailInfoVO> articleListRes = new ListResDTO<>();
        if (articleCommentInfoResWithUserDTOS != null) {
            articleListRes.setCurrent(uArticleSubCommentListParamsDTO.getCurrent());
            articleListRes.setPageSize(uArticleSubCommentListParamsDTO.getPageSize());
            articleListRes.setList(articleCommentInfoResWithUserDTOS);
            articleListRes.setTotal(total);
        }

        return Result.success("获取成功", articleListRes);
    }
}




