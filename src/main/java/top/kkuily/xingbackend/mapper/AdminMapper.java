package top.kkuily.xingbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.kkuily.xingbackend.model.po.Admin;

/**
* @author 小K
* @description 针对表【admin】的数据库操作Mapper
* @createDate 2023-05-18 11:15:44
* @Entity top.kkuily.xingbackend.model.entity.Admin
*/
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

}




