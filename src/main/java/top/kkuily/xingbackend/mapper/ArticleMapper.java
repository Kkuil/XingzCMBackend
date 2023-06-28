package top.kkuily.xingbackend.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;
import top.kkuily.xingbackend.model.dto.response.article.admin.ArticleInfoResWithAdminDTO;
import top.kkuily.xingbackend.model.dto.response.article.user.ArticleInfoResWithUserDTO;
import top.kkuily.xingbackend.model.po.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.article.detail.ArticleDetailInfoVO;
import top.kkuily.xingbackend.model.vo.article.detail.AuthorDetailInfoVO;
import top.kkuily.xingbackend.model.vo.article.list.ArticleListFilterVO;
import top.kkuily.xingbackend.model.vo.article.list.ArticleListParamsVO;
import top.kkuily.xingbackend.model.vo.article.list.ArticleListSortVO;

import java.util.List;

/**
 * @author 小K
 * @description 针对表【article】的数据库操作Mapper
 * @createDate 2023-06-08 10:16:55
 * @Entity top.kkuily.xingbackend.model.po.Article
 */
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * @param params ArticleListParamsVO
     * @param sort   ArticleListSortVO
     * @param filter ArticleListFilterVO
     * @param page   ListPageVO
     * @return List<ChatGPTModelInfoResDTO>
     * @description 带有limit的分页查询
     */
    List<ArticleInfoResWithAdminDTO> listArticlesWithLimit(
            @Param("params") ArticleListParamsVO params,
            @Param("sort") ArticleListSortVO sort,
            @Param("filter") ArticleListFilterVO filter,
            @Param("page") ListPageVO page
    );

    /**
     * @param params ArticleListParamsVO
     * @param sort   ArticleListSortVO
     * @param filter ArticleListFilterVO
     * @param page   ListPageVO
     * @return List<ChatGPTModelInfoResDTO>
     * @description 不带有limit的分页查询
     */
    Integer listArticlesWithNotLimit(
            @Param("params") ArticleListParamsVO params,
            @Param("sort") ArticleListSortVO sort,
            @Param("filter") ArticleListFilterVO filter,
            @Param("page") ListPageVO page
    );

    /**
     * @return List<ChatGPTModelInfoResDTO>
     * @description 带有limit的分页查询
     */
    List<ArticleInfoResWithUserDTO> listArticlesWithLimitAndUser(
            @PathVariable("tagId") int tagId,
            @PathVariable("categoryId") String categoryId,
            @PathVariable("current") int current,
            @PathVariable("pageSize") int pageSize,
            @PathVariable("userId") String userId
    );

    /**
     * @return List<ChatGPTModelInfoResDTO>
     * @description 不带有limit的分页查询
     */
    Integer listArticlesWithNotLimitAndUser(
            @PathVariable("tagId") int tagId,
            @PathVariable("categoryId") String categoryId
    );

    /**
     * @param userId    String
     * @param articleId String
     * @return ArticleDetailInfoVO
     * @description 找出详情页文章的信息
     */
    ArticleDetailInfoVO selectArticleDetailInfo(@PathVariable("articleId") String articleId, @PathVariable("userId") String userId);

    /**
     * @param articleId String
     * @return User
     * @description 通过文章ID获取作者ID，因为这里之前已经使用过article表中的userId，所以这里就不删了
     */
    AuthorDetailInfoVO selectUserById(@PathVariable("articleId") String articleId);
}
