package top.kkuily.xingbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.kkuily.xingbackend.model.po.ArticleStatus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 小K
* @description 针对表【article_status】的数据库操作Mapper
* @createDate 2023-05-18 11:21:02
* @Entity top.kkuily.xingbackend.model.entity.ArticleStatusEnums
*/
@Mapper
public interface ArticleStatusMapper extends BaseMapper<ArticleStatus> {

}




