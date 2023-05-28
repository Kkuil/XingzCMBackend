package top.kkuily.xingbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import top.kkuily.xingbackend.model.vo.ListParamsVo;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginPhoneBody;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginAccountBody;
import top.kkuily.xingbackend.model.po.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListFilterVo;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListParamsVo;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListSortVo;
import top.kkuily.xingbackend.utils.Result;

/**
* @author 小K
* @description 针对表【admin】的数据库操作Service
* @createDate 2023-05-18 11:15:44
*/
public interface IAdminService extends IService<Admin> {

    /**
     * @description 管理员账号登录服务
     * @author 小K
     * @param adminLoginAccountBody AdminLoginAccountBody
     * @return Result
     */
    Result loginWithAccount(HttpServletResponse response, AdminLoginAccountBody adminLoginAccountBody);

    /**
     * @description 管理员手机号登录服务
     * @author 小K
     * @param adminLoginPhoneBody AdminLoginPhoneBody
     * @return Result
     */
    Result loginWithPhone(HttpServletResponse response, AdminLoginPhoneBody adminLoginPhoneBody);

    /**
     * @description 管理员鉴权服务
     * @author 小K
     * @param response HttpServletRequest
     * @return Result
     */
    Result auth(HttpServletRequest response);

    /**
     * @description 分页查询
     * @author 小K
     * @param adminListParams ListParamsVo
     * @return Result
     */
    Result getList(ListParamsVo<AdminListParamsVo, AdminListSortVo, AdminListFilterVo> adminListParams);
}
