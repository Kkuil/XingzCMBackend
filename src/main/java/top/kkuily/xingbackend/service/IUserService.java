package top.kkuily.xingbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import top.kkuily.xingbackend.model.dto.request.commons.ListParams;
import top.kkuily.xingbackend.model.dto.request.user.UserLoginAccountBody;
import top.kkuily.xingbackend.model.dto.request.user.UserLoginPhoneBody;
import top.kkuily.xingbackend.model.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kkuily.xingbackend.utils.Result;

/**
 * @author 小K
 * @description 针对表【user】的数据库操作Service
 * @createDate 2023-05-18 11:15:44
 */
public interface IUserService extends IService<User> {

    /**
     * @author 小K
     * @description 用户账号登录服务
     * @param userLoginBody UserLoginAccountBody
     * @return Result
     */
    Result loginWithAccount(HttpServletResponse response, UserLoginAccountBody userLoginBody);

    /**
     * @author 小K
     * @description 用户鉴权服务
     * @param response HttpServletRequest
     * @return Result
     */
    Result auth(HttpServletRequest response);

    /**
     * @author 小K
     * @description 分页查询
     * @param userListParams UserListParams
     * @return Result
     */
    Result getList(ListParams userListParams);

    /**
     * @author 小K
     * @description 用户手机号登录服务
     * @param userLoginPhoneBody UserLoginPhoneBody
     * @return Result
     */
    Result loginWithPhone(HttpServletResponse response, UserLoginPhoneBody userLoginPhoneBody);
}
