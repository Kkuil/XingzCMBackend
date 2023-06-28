package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kkuily.xingbackend.model.po.ArticleTag;
import top.kkuily.xingbackend.mapper.ArticleTagMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.service.IArticleTagService;

/**
* @author 小K
* @description 针对表【article_tag】的数据库操作Service实现
* @createDate 2023-06-14 09:51:59
*/
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
    implements IArticleTagService {

}




