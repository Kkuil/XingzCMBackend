package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kkuily.xingbackend.model.po.ArticleStatus;
import top.kkuily.xingbackend.service.IArticleStatusService;
import top.kkuily.xingbackend.mapper.ArticleStatusMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【article_status】的数据库操作Service实现
* @createDate 2023-05-18 11:21:02
*/
@Service
public class ArticleStatusServiceImpl extends ServiceImpl<ArticleStatusMapper, ArticleStatus>
    implements IArticleStatusService {

}




