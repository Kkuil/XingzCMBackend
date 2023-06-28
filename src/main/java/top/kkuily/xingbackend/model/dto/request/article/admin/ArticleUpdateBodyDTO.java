package top.kkuily.xingbackend.model.dto.request.article;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.kkuily.xingbackend.model.po.Article;

/**
 * @author 小K
 * @description 更新文章的传输数据
 */
@Data
public class ArticleUpdateBodyDTO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 状态ID
     */
    private Integer statusId;

    /**
     * 文章封面图
     */
    private String cover;

    /**
     * @param article Article
     */
    public void convertTo(Article article) {
        BeanUtils.copyProperties(this, article);
    }
}
