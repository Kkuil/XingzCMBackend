package top.kkuily.xingbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.kkuily.xingbackend.model.po.RoleAuth;

/**
* @author 小K
* @description 针对表【role_auth】的数据库操作Mapper
* @createDate 2023-05-22 12:31:03
* @Entity top.kkuily.xingbackend.RoleAuth
*/
@Mapper
public interface RoleAuthMapper extends BaseMapper<RoleAuth> {

}




