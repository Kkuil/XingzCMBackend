package top.kkuily.xingbackend.model.dto.response.article.admin;

import lombok.Data;

import java.util.Date;

/**
 * @author 小K
 * @description 文章分页查询返回类
 */
@Data
public class ArticleInfoResWithAdminDTO {
    /**
     * 文章ID
     */
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 分类IDS
     */
    private String categoryIds;

    /**
     * 状态ID
     */
    private String statusId;

    /**
     * 文章封面图
     */
    private String cover;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）
     */
    private Date modifiedTime;

}
