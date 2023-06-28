package top.kkuily.xingbackend.model.vo.article.detail;

import lombok.Data;
import top.kkuily.xingbackend.model.po.Article;

import java.util.List;

/**
 * @author 小K
 * @description 用户获取文章详情中作者动态的实体
 */
@Data
public class UserArticleDetailInfoVO {
    /**
     * 作者最新前五篇文章
     */
    private List<Article> newestArticles;

    /**
     * 作者置顶前五篇文章
     */
    private List<Article> pinnedArticles;
}
