package top.kkuily.xingbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.kkuily.xingbackend.model.dto.response.user.UserAuthInfoResDTO;
import top.kkuily.xingbackend.model.dto.response.user.UserInfoResDTO;
import top.kkuily.xingbackend.model.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListFilterVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListParamsVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListSortVO;

import java.util.List;

/**
 * @author 小K
 * @description 针对表【user】的数据库操作Mapper
 * @createDate 2023-05-18 11:21:38
 * @Entity top.kkuily.xingbackend.model.entity.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * @param params UserListParamsVO
     * @param sort   UserListSortVO
     * @param filter UserListFilterVO
     * @param page   ListPageVO
     * @return List<UserInfoResDTO>
     * @description 带有limit的分页查询
     */
    List<UserInfoResDTO> listUsersWithLimit(
            @Param("params") UserListParamsVO params,
            @Param("sort") UserListSortVO sort,
            @Param("filter") UserListFilterVO filter,
            @Param("page") ListPageVO page
    );

    /**
     * @param params UserListParamsVO
     * @param sort   UserListSortVO
     * @param filter UserListFilterVO
     * @param page   ListPageVO
     * @return List<UserInfoResDTO>
     * @description 不带有limit的分页查询
     */
    Integer listUsersWithNotLimit(
            @Param("params") UserListParamsVO params,
            @Param("sort") UserListSortVO sort,
            @Param("filter") UserListFilterVO filter,
            @Param("page") ListPageVO page
    );
}




