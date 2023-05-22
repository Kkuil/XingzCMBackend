package top.kkuily.xingbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.kkuily.xingbackend.model.po.UserArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 小K
* @description 针对表【user_article】的数据库操作Mapper
* @createDate 2023-05-18 11:21:49
* @Entity top.kkuily.xingbackend.model.entity.UserArticle
*/
@Mapper
public interface UserArticleMapper extends BaseMapper<UserArticle> {

}




