package top.kkuily.xingbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.expression.AccessException;
import top.kkuily.xingbackend.model.dto.request.article.admin.ArticleAddBodyDTO;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleListParamsDTO;
import top.kkuily.xingbackend.model.po.Article;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.article.list.ArticleListFilterVO;
import top.kkuily.xingbackend.model.vo.article.list.ArticleListParamsVO;
import top.kkuily.xingbackend.model.vo.article.list.ArticleListSortVO;
import top.kkuily.xingbackend.utils.Result;

/**
 * @author 小K
 * @description 针对表【article】的数据库操作Service
 * @createDate 2023-05-30 11:50:08
 */
public interface IArticleService extends IService<Article> {

    /**
     * @param articleListParams ListParamsVO<ArticleListParamsVO, ArticleListSortVO, ArticleListFilterVO>
     * @return Result
     * @description 管理员分页获取文章
     */
    Result adminGetList(ListParamsVO<ArticleListParamsVO, ArticleListSortVO, ArticleListFilterVO> articleListParams);

    /**
     * @param uArticleListParams UArticleListParamsDTO
     * @return Result
     * @description 用户分页获取文章
     */
    Result userGetList(UArticleListParamsDTO uArticleListParams, HttpServletRequest request);

    /**
     * @param articleId String
     * @param request   HttpServletRequest
     * @return Result
     * @description 用户查看文章详情
     */
    Result getArticleAndUserInfoByIdWithUser(String articleId, HttpServletRequest request) throws AccessException;

    /**
     * @param articleAddBodyDTO ArticleAddBodyDTO
     * @param request           HttpServletRequest
     * @return Result
     * @description 发布文章
     */
    Result publish(ArticleAddBodyDTO articleAddBodyDTO, HttpServletRequest request);

    /**
     * @param article String
     * @param request HttpServletRequest
     * @return Result
     * @description 点赞获取消点赞
     */
    Result like(String articleId, HttpServletRequest request);

    /**
     * @param article String
     * @param request HttpServletRequest
     * @return Result
     * @description 收藏获取消收藏
     */
    Result collect(String articleId, HttpServletRequest request);
}
