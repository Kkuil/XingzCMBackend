package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kkuily.xingbackend.model.po.Article;
import top.kkuily.xingbackend.service.IArticleService;
import top.kkuily.xingbackend.mapper.ArticleMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【article】的数据库操作Service实现
* @createDate 2023-05-30 11:50:08
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements IArticleService {

}




