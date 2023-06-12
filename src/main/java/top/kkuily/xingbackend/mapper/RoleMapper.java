package top.kkuily.xingbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.kkuily.xingbackend.model.dto.response.role.RoleInfoResDTO;
import top.kkuily.xingbackend.model.po.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.role.list.RoleListFilterVO;
import top.kkuily.xingbackend.model.vo.role.list.RoleListParamsVO;
import top.kkuily.xingbackend.model.vo.role.list.RoleListSortVO;

import java.util.List;

/**
 * @author 小K
 * @description 针对表【role】的数据库操作Mapper
 * @createDate 2023-05-21 16:12:27
 * @Entity top.kkuily.xingbackend.model.po.Role
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * @param params RoleListParamsVO
     * @param sort   RoleListSortVO
     * @param filter RoleListFilterVO
     * @param page   ListPageVO
     * @return List<RoleInfoResDTO>
     * @description 带有limit的分页查询
     */
    List<RoleInfoResDTO> listRolesWithLimit(
            @Param("params") RoleListParamsVO params,
            @Param("sort") RoleListSortVO sort,
            @Param("filter") RoleListFilterVO filter,
            @Param("page") ListPageVO page
    );

    /**
     * @param params RoleListParamsVO
     * @param sort   RoleListSortVO
     * @param filter RoleListFilterVO
     * @param page   ListPageVO
     * @return List<RoleInfoResDTO>
     * @description 不带有limit的分页查询
     */
    Integer listRolesWithNotLimit(
            @Param("params") RoleListParamsVO params,
            @Param("sort") RoleListSortVO sort,
            @Param("filter") RoleListFilterVO filter,
            @Param("page") ListPageVO page
    );
}




