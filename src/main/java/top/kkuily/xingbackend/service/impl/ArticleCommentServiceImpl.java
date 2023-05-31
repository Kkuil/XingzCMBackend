package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kkuily.xingbackend.model.po.ArticleComment;
import top.kkuily.xingbackend.service.IArticleCommentService;
import top.kkuily.xingbackend.mapper.ArticleCommentMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【article_comment】的数据库操作Service实现
* @createDate 2023-05-30 12:32:38
*/
@Service
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment>
    implements IArticleCommentService {

}




