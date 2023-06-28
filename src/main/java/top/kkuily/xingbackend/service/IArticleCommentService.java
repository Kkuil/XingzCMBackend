package top.kkuily.xingbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleCommentParamsDTO;
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
     * @param uArticleCommentParamsDTO UArticleCommentParamsDTO
     * @param request                  HttpServletRequest
     * @return Result
     * @description 用户评论
     */
    Result comment(UArticleCommentParamsDTO uArticleCommentParamsDTO, HttpServletRequest request);
}
