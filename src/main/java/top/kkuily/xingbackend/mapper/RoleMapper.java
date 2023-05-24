package top.kkuily.xingbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.kkuily.xingbackend.model.po.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 小K
* @description 针对表【role】的数据库操作Mapper
* @createDate 2023-05-21 16:12:27
* @Entity top.kkuily.xingbackend.model.po.Role
*/
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

}




