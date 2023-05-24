package top.kkuily.xingbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.kkuily.xingbackend.model.po.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 小K
* @description 针对表【department】的数据库操作Mapper
* @createDate 2023-05-21 13:03:51
* @Entity top.kkuily.xingbackend.model.po.Department
*/
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

}




