package top.kkuily.xingbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginAccountBodyDTO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginPhoneBodyDTO;
import top.kkuily.xingbackend.model.po.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListFilterVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListParamsVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListSortVO;
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
     * @param adminLoginAccountBody AdminLoginAccountBodyDTO
     * @return Result
     */
    Result loginWithAccount(HttpServletResponse response, AdminLoginAccountBodyDTO adminLoginAccountBody);

    /**
     * @description 管理员手机号登录服务
     * @author 小K
     * @param adminLoginPhoneBody AdminLoginPhoneBodyDTO
     * @return Result
     */
    Result loginWithPhone(HttpServletResponse response, AdminLoginPhoneBodyDTO adminLoginPhoneBody);

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
     * @param adminListParams ListParamsVO
     * @return Result
     */
    Result getList(ListParamsVO<AdminListParamsVO, AdminListSortVO, AdminListFilterVO> adminListParams);
}
