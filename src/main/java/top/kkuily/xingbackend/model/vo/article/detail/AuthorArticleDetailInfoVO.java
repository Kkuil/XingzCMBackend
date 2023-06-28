package top.kkuily.xingbackend.model.vo.article.detail;

import lombok.Data;
import top.kkuily.xingbackend.model.bo.Article.ArticleLinkBO;
import top.kkuily.xingbackend.model.po.Article;

import java.util.List;

/**
 * @author 小K
 * @description 用户获取文章详情中作者动态的实体
 */
@Data
public class AuthorArticleDetailInfoVO {
    /**
     * 作者最新前五篇文章
     */
    private List<ArticleLinkBO> latestArticles;

    /**
     * 作者置顶前五篇文章
     */
    private List<ArticleLinkBO> pinnedArticles;
}
