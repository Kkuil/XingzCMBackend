package top.kkuily.xingbackend.mapper;

import org.springframework.web.bind.annotation.PathVariable;
import top.kkuily.xingbackend.model.po.ArticleComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.kkuily.xingbackend.model.vo.article.detail.CommentDetailInfoVO;
import top.kkuily.xingbackend.model.vo.article.detail.SubCommentDetailInfoVO;

import java.util.List;

/**
 * @author 小K
 * @description 针对表【article_comment】的数据库操作Mapper
 * @createDate 2023-06-16 16:14:06
 * @Entity top.kkuily.xingbackend.model.po.ArticleComment
 */
public interface ArticleCommentMapper extends BaseMapper<ArticleComment> {
    /**
     * @param articleId String
     * @param userId    String
     * @param current   int
     * @param pageSize  int
     * @return List<CommentDetailInfoVO>
     * @description 通过文章ID获取指定数目的评论（不包括子级评论）
     */
    List<CommentDetailInfoVO> listArticleCommentWithLimit(
            @PathVariable("articleId") String articleId,
            @PathVariable("userId") String userId,
            @PathVariable("current") int current,
            @PathVariable("pageSize") int pageSize
    );

    /**
     * @param articleId String
     * @param userId    String
     * @return List<CommentDetailInfoVO>
     * @description 通过文章ID获取评论总数数目
     */
    Integer listArticleCommentWithNotLimit(
            @PathVariable("articleId") String articleId
    );

    /**
     * @param commentId int
     * @param id        String
     * @param current   Integer
     * @param pageSize  Integer
     * @return List<CommentDetailInfoVO>
     * @description 通过评论ID获取指定数目的子级评论
     */
    List<SubCommentDetailInfoVO> listArticleSubCommentWithLimit(int commentId, String userId, Integer current, Integer pageSize);

    /**
     * @param commentId int
     * @return int
     * @description 通过评论ID获取子级评论总数
     */
    int listArticleSubCommentWithNotLimit(int commentId);
}




