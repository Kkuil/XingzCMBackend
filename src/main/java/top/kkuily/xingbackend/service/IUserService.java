package top.kkuily.xingbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.expression.AccessException;
import top.kkuily.xingbackend.model.dto.request.user.UserLoginAccountBodyDTO;
import top.kkuily.xingbackend.model.dto.request.user.UserLoginPhoneBodyDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kkuily.xingbackend.model.po.User;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListFilterVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListParamsVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListSortVO;
import top.kkuily.xingbackend.utils.Result;

/**
 * @author 小K
 * @description 针对表【user】的数据库操作Service
 * @createDate 2023-05-18 11:15:44
 */
public interface IUserService extends IService<User> {

    /**
     * @param userLoginBody UserLoginAccountBodyDTO
     * @return Result
     * @description 用户账号登录服务
     * @author 小K
     */
    Result loginWithAccount(HttpServletResponse response, UserLoginAccountBodyDTO userLoginBody);

    /**
     * @param userLoginPhoneBody UserLoginPhoneBodyDTO
     * @return Result
     * @description 用户手机号登录服务
     * @author 小K
     */
    Result loginRegistryWithPhone(HttpServletResponse response, UserLoginPhoneBodyDTO userLoginPhoneBody);

    /**
     * @param response HttpServletRequest
     * @return Result
     * @description 用户鉴权服务
     * @author 小K
     */
    Result auth(HttpServletRequest response) throws AccessException;

    /**
     * @param userListParams UserListParams
     * @return Result
     * @description 分页查询
     * @author 小K
     */
    Result getList(ListParamsVO<UserListParamsVO, UserListSortVO, UserListFilterVO> userListParams);

    /**
     * @param id      String
     * @param request HttpServletRequest
     * @return Result
     * @description 用户通过id获取信息服务接口
     */
    Result get(String id, HttpServletRequest request);
}
