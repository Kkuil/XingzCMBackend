package top.kkuily.xingbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.kkuily.xingbackend.model.po.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 小K
* @description 针对表【article】的数据库操作Mapper
* @createDate 2023-05-18 11:20:42
* @Entity top.kkuily.xingbackend.model.entity.Article
*/
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

}




