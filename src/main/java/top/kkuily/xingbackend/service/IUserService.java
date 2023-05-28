package top.kkuily.xingbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginPhoneBody;
import top.kkuily.xingbackend.model.dto.request.user.UserLoginAccountBody;
import top.kkuily.xingbackend.model.dto.request.user.UserLoginPhoneBody;
import top.kkuily.xingbackend.model.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kkuily.xingbackend.model.vo.ListParamsVo;
import top.kkuily.xingbackend.model.vo.user.list.UserListFilterVo;
import top.kkuily.xingbackend.model.vo.user.list.UserListParamsVo;
import top.kkuily.xingbackend.model.vo.user.list.UserListSortVo;
import top.kkuily.xingbackend.utils.Result;

/**
 * @author 小K
 * @description 针对表【user】的数据库操作Service
 * @createDate 2023-05-18 11:15:44
 */
public interface IUserService extends IService<User> {

    /**
     * @description 用户账号登录服务
     * @author 小K
     * @param userLoginBody UserLoginAccountBody
     * @return Result
     */
    Result loginWithAccount(HttpServletResponse response, UserLoginAccountBody userLoginBody);

    /**
     * @description 用户手机号登录服务
     * @author 小K
     * @param adminLoginPhoneBody AdminLoginPhoneBody
     * @return Result
     */
    Result registryWithPhone(HttpServletResponse response, AdminLoginPhoneBody adminLoginPhoneBody);

    /**
     * @description 用户鉴权服务
     * @author 小K
     * @param response HttpServletRequest
     * @return Result
     */
    Result auth(HttpServletRequest response);

    /**
     * @description 分页查询
     * @author 小K
     * @param userListParams UserListParams
     * @return Result
     */
    Result getList(ListParamsVo<UserListParamsVo, UserListSortVo, UserListFilterVo> userListParams);

}
