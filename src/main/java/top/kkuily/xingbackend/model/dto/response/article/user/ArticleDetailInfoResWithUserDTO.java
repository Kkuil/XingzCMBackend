package top.kkuily.xingbackend.model.dto.response.article.user;

import lombok.Data;
import top.kkuily.xingbackend.model.vo.article.detail.ArticleDetailInfoVO;
import top.kkuily.xingbackend.model.vo.article.detail.AuthorArticleDetailInfoVO;
import top.kkuily.xingbackend.model.vo.article.detail.AuthorDetailInfoVO;
import top.kkuily.xingbackend.model.vo.article.detail.CommentDetailInfoVO;

import java.util.List;

/**
 * @author 小K
 * @description 用户获取文章详情信息实体
 */
@Data
public class ArticleDetailInfoResWithUserDTO {
    /**
     * 文章详情
     */
    ArticleDetailInfoVO articleDetailInfo;
    /**
     * 作者详情
     */
    AuthorDetailInfoVO authorDetailInfo;
    /**
     * 作者动态详情
     */
    AuthorArticleDetailInfoVO authorArticleDetailInfo;
    /**
     * 评论详情
     */
    List<CommentDetailInfoVO> commentDetailInfoVO;

    /**
     * @param articleDetailInfo       ArticleDetailInfoVO
     * @param authorDetailInfo        AuthorDetailInfoVO
     * @param authorArticleDetailInfo AuthorArticleDetailInfoVO
     * @description 批量设置文章
     */
    public void allSet(
            ArticleDetailInfoVO articleDetailInfo,
            AuthorDetailInfoVO authorDetailInfo,
            AuthorArticleDetailInfoVO authorArticleDetailInfo,
            List<CommentDetailInfoVO> commentDetailInfoVO
    ) {
        this.setArticleDetailInfo(articleDetailInfo);
        this.setAuthorDetailInfo(authorDetailInfo);
        this.setAuthorArticleDetailInfo(authorArticleDetailInfo);
        this.setCommentDetailInfoVO(commentDetailInfoVO);
    }
}
