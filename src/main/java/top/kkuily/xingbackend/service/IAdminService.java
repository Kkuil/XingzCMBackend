package top.kkuily.xingbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import top.kkuily.xingbackend.model.dto.request.ListParams;
import top.kkuily.xingbackend.model.dto.request.AdminLoginBody;
import top.kkuily.xingbackend.model.po.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kkuily.xingbackend.utils.Result;

/**
* @author 小K
* @description 针对表【admin】的数据库操作Service
* @createDate 2023-05-18 11:15:44
*/
public interface IAdminService extends IService<Admin> {

    /**
     * @author 小K
     * @description 管理员登录服务
     * @param adminLoginBody AdminLoginBody
     * @return Result
     */
    Result login(HttpServletResponse response, AdminLoginBody adminLoginBody);

    /**
     * @author 小K
     * @description 管理员鉴权服务
     * @param response HttpServletRequest
     * @return Result
     */
    Result auth(HttpServletRequest response);

    /**
     * @author 小K
     * @description 分页查询
     * @param adminListParams AdminListParams
     * @return Result
     */
    Result getList(ListParams adminListParams);
}
