package top.kkuily.xingbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.kkuily.xingbackend.model.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 小K
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-05-18 11:21:38
* @Entity top.kkuily.xingbackend.model.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




