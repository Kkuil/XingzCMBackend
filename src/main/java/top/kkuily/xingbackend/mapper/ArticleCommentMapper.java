package top.kkuily.xingbackend.mapper;

import org.springframework.web.bind.annotation.PathVariable;
import top.kkuily.xingbackend.model.po.ArticleComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.kkuily.xingbackend.model.vo.article.detail.CommentDetailInfoVO;

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
     * @return List<CommentDetailInfoVO>
     * @description 通过文章ID获取指定数目的评论（不包括子级评论）
     */
    List<CommentDetailInfoVO> selectCommentInfoLimitById(
            @PathVariable("articleId") String articleId,
            @PathVariable("skip") int skip,
            @PathVariable("pageSize") int pageSize
    );
}




