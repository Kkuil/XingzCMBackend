package top.kkuily.xingbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.kkuily.xingbackend.model.dto.response.admin.AdminAuthInfoResDTO;
import top.kkuily.xingbackend.model.dto.response.admin.AdminInfoResDTO;
import top.kkuily.xingbackend.model.po.Admin;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListFilterVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListParamsVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListSortVO;

import java.util.List;

/**
 * @author 小K
 * @description 针对表【admin】的数据库操作Mapper
 * @createDate 2023-05-18 11:15:44
 * @Entity top.kkuily.xingbackend.model.entity.Admin
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * @param id String
     * @return AdminAuthInfoResDTO
     * @description 验证操作
     */
    AdminAuthInfoResDTO selectAuthInfo(@Param("id") String id);

    /**
     * @param params AdminListParamsVO
     * @param sort   AdminListSortVO
     * @param filter AdminListFilterVO
     * @param page   ListPageVO
     * @return List<AdminInfoResDTO>
     * @description 带有limit的分页查询
     */
    List<AdminInfoResDTO> listAdminsWithLimit(
            @Param("params") AdminListParamsVO params,
            @Param("sort") AdminListSortVO sort,
            @Param("filter") AdminListFilterVO filter,
            @Param("page") ListPageVO page
    );

    /**
     * @param params AdminListParamsVO
     * @param sort   AdminListSortVO
     * @param filter AdminListFilterVO
     * @param page   ListPageVO
     * @return List<AdminInfoResDTO>
     * @description 不带有limit的分页查询
     */
    Integer listAdminsWithNotLimit(
            @Param("params") AdminListParamsVO params,
            @Param("sort") AdminListSortVO sort,
            @Param("filter") AdminListFilterVO filter,
            @Param("page") ListPageVO page
    );
}




