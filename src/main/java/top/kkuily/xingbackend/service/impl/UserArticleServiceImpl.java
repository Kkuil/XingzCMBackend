package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kkuily.xingbackend.model.po.UserArticle;
import top.kkuily.xingbackend.service.IUserArticleService;
import top.kkuily.xingbackend.mapper.UserArticleMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【user_article】的数据库操作Service实现
* @createDate 2023-05-18 11:21:49
*/
@Service
public class UserArticleServiceImpl extends ServiceImpl<UserArticleMapper, UserArticle>
    implements IUserArticleService {

}




