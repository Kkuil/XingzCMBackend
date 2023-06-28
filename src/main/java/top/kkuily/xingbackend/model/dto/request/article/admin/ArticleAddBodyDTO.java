package top.kkuily.xingbackend.model.dto.request.article.admin;

import lombok.Data;
import org.springframework.lang.Nullable;
import top.kkuily.xingbackend.model.po.Article;

/**
 * @author 小K
 * @description 增加角色的DTO类
 */
@Data
public class ArticleAddBodyDTO {
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
     * 标签信息（例如：[1, 2, 3]记录了标签的ID）
     */
    private String tagIds;

    /**
     * 转换为本类方法
     *
     * @param article Article
     */
    public void convertTo(Article article) {
        article.setTitle(this.getTitle());
        article.setContent(this.getContent());
        article.setStatusId(this.getStatusId());
        article.setCover(this.getCover());
    }
}
