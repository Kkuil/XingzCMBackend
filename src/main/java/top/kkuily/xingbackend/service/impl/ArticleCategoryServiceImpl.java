package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kkuily.xingbackend.model.po.ArticleCategory;
import top.kkuily.xingbackend.mapper.ArticleCategoryMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.service.IArticleCategoryService;

/**
* @author 小K
* @description 针对表【article_category】的数据库操作Service实现
* @createDate 2023-06-14 09:51:55
*/
@Service
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryMapper, ArticleCategory>
    implements IArticleCategoryService {

}




