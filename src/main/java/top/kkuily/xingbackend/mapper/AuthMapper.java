package top.kkuily.xingbackend.mapper;

import org.apache.ibatis.annotations.Param;
import top.kkuily.xingbackend.model.po.Auth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 小K
* @description 针对表【auth】的数据库操作Mapper
* @createDate 2023-05-31 11:00:28
* @Entity top.kkuily.xingbackend.model.po.Auth
*/
public interface AuthMapper extends BaseMapper<Auth> {
    /**
     * @param ids String
     * @return List<String>
     * @description 通过多个权限id查询标权限名
     */
    List<String> selectAuthDescriptionListByBatchId(@Param("ids") List<String> ids);
}
