package top.kkuily.xingbackend.mapper;

import org.springframework.web.bind.annotation.PathVariable;
import top.kkuily.xingbackend.model.bo.Article.ArticleLinkBO;
import top.kkuily.xingbackend.model.enums.SortedTypeEnums;
import top.kkuily.xingbackend.model.enums.UserArticleTypeEnums;
import top.kkuily.xingbackend.model.po.Article;
import top.kkuily.xingbackend.model.po.UserArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author 小K
 * @description 针对表【user_article】的数据库操作Mapper
 * @createDate 2023-06-15 18:11:19
 * @Entity top.kkuily.xingbackend.model.po.UserArticle
 */
public interface UserArticleMapper extends BaseMapper<UserArticle> {

    /**
     * @param userId               String
     * @param userArticleTypeEnums int
     * @param skip                 int
     * @param pageSize             int
     * @param sortedType           String
     * @return List<Article>
     * @description 通过用户id获取当前用户指定文章类型的数目
     */
    List<ArticleLinkBO> selectSpecificTypeArticlesById(
            @PathVariable("userId") String userId,
            @PathVariable("userArticleTypeEnums") int userArticleTypeEnums,
            @PathVariable("skip") int skip,
            @PathVariable("pageSize") int pageSize,
            @PathVariable("sortedColumn") String sortedColumn,
            @PathVariable("sortedType") SortedTypeEnums sortedType
    );

}




