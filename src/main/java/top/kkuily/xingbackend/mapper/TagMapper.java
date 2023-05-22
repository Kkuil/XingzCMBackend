package top.kkuily.xingbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.kkuily.xingbackend.model.po.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 小K
* @description 针对表【tag】的数据库操作Mapper
* @createDate 2023-05-18 11:21:27
* @Entity top.kkuily.xingbackend.model.entity.Tag
*/
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}




