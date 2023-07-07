package top.kkuily.xingbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.expression.AccessException;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleCommentAddParamsDTO;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleCommentListParamsDTO;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleSubCommentListParamsDTO;
import top.kkuily.xingbackend.model.po.ArticleComment;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kkuily.xingbackend.utils.Result;

/**
 * @author 小K
 * @description 针对表【article_comment】的数据库操作Service
 * @createDate 2023-06-16 16:14:06
 */
public interface IArticleCommentService extends IService<ArticleComment> {

    /**
     * @param uArticleCommentParamsDTO UArticleCommentAddParamsDTO
     * @param request                  HttpServletRequest
     * @return Result
     * @description 用户评论
     */
    Result comment(UArticleCommentAddParamsDTO uArticleCommentParamsDTO, HttpServletRequest request);

    /**
     * @param uArticleCommentListParamsDTO UArticleCommentListParamsDTO
     * @param request                      HttpServletRequest
     * @return Result
     * @description 获取评论列表
     */
    Result getList(UArticleCommentListParamsDTO uArticleCommentListParamsDTO, HttpServletRequest request) throws AccessException;

    /**
     * @param uArticleSubCommentListParamsDTO UArticleSubCommentListParamsDTO
     * @param request                         HttpServletRequest
     * @return Result
     * @description 通过评论ID获取子评论
     */
    Result getSubCommentById(UArticleSubCommentListParamsDTO uArticleSubCommentListParamsDTO, HttpServletRequest request);
}
