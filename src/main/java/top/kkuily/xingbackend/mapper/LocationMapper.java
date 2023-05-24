package top.kkuily.xingbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.kkuily.xingbackend.model.po.Location;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 小K
* @description 针对表【location】的数据库操作Mapper
* @createDate 2023-05-21 13:05:14
* @Entity top.kkuily.xingbackend.model.po.Location
*/
@Mapper
public interface LocationMapper extends BaseMapper<Location> {

}




