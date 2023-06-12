package top.kkuily.xingbackend.mapper;

import org.apache.ibatis.annotations.Param;
import top.kkuily.xingbackend.model.dto.response.article.ArticleInfoResDTO;
import top.kkuily.xingbackend.model.po.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
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
    List<ArticleInfoResDTO> listArticlesWithLimit(
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
}
