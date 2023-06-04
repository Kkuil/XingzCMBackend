package top.kkuily.xingbackend.service;

import top.kkuily.xingbackend.model.po.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListFilterVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListParamsVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListSortVO;
import top.kkuily.xingbackend.model.vo.role.list.RoleListFilterVO;
import top.kkuily.xingbackend.model.vo.role.list.RoleListParamsVO;
import top.kkuily.xingbackend.model.vo.role.list.RoleListSortVO;
import top.kkuily.xingbackend.utils.Result;

/**
* @author 小K
* @description 针对表【role】的数据库操作Service
* @createDate 2023-05-21 16:12:27
*/
public interface IRoleService extends IService<Role> {

    Result getList(ListParamsVO<RoleListParamsVO, RoleListSortVO, RoleListFilterVO> roleListParams);
}
