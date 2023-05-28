package top.kkuily.xingbackend.web.controller;

import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.kkuily.xingbackend.constant.commons.Api;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginPhoneBody;
import top.kkuily.xingbackend.model.dto.request.user.UserLoginAccountBody;
import top.kkuily.xingbackend.model.dto.request.user.UserLoginPhoneBody;
import top.kkuily.xingbackend.model.vo.ListPageVo;
import top.kkuily.xingbackend.model.vo.ListParamsVo;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListFilterVo;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListParamsVo;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListSortVo;
import top.kkuily.xingbackend.model.vo.user.list.UserListFilterVo;
import top.kkuily.xingbackend.model.vo.user.list.UserListParamsVo;
import top.kkuily.xingbackend.model.vo.user.list.UserListSortVo;
import top.kkuily.xingbackend.service.IUserService;
import top.kkuily.xingbackend.utils.ErrorType;
import top.kkuily.xingbackend.utils.PicCaptcha;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.SmsCaptcha;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.kkuily.xingbackend.constant.commons.Api.PHONE_REG;

/**
 * @author 小K
 * @description 用户相关接口
 */
@RestController
@Slf4j
public class UserController {
    @Resource
    private IUserService userService;

    /**
     * 账号登录接口
     *
     * @param response       HttpServletResponse
     * @param userLoginBody userLoginBody
     * @return Result
     */
    @PostMapping("user-login-account")
    public Result loginWithAccount(HttpServletResponse response, @RequestBody UserLoginAccountBody userLoginBody) {
        return userService.loginWithAccount(response, userLoginBody);
    }

    /**
     * 权限校验端口
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @PostMapping("user-auth")
    public Result auth(HttpServletRequest request) {
        return userService.auth(request);
    }

    /**
     * @description 获取图形验证码
     * @return Result
     */
    @GetMapping("user-captcha")
    public Result getCaptcha() throws IOException {
        Map<String, String> codeInfo = PicCaptcha.generate();
        return Result.success("获取验证码成功", codeInfo);
    }

    /**
     * @description 发送验证码
     * @author 小K
     * @param phone phone
     * @return Result
     */
    @GetMapping("user-sms")
    public Result getSmsCaptcha(String phone) throws ExecutionException, InterruptedException {
        if (phone == null) {
            return Result.fail(401, "非法请求", ErrorType.ERROR_MESSAGE);
        }
        Pattern reg = Pattern.compile(PHONE_REG);
        Matcher matcher = reg.matcher(phone);
        if (!matcher.matches()) {
            return Result.fail(403, "手机号格式错误，请检查手机号格式", ErrorType.NOTIFICATION);
        } else {
            int sms = SmsCaptcha.send(phone);
            if (sms != 0) {
                return Result.success("发送成功，验证码有效期为三分钟", true);
            } else {
                return Result.fail(500, "服务器异常，请联系站长进行修复后再试", ErrorType.NOTIFICATION);
            }
        }
    }

    /**
     * @description 用户手机号注册接口
     *
     * @param response            HttpServletResponse
     * @param adminLoginPhoneBody AdminLoginPhoneBody
     * @return Result
     */
    @PostMapping("user-registry-phone")
    public Result loginWithPhone(HttpServletResponse response, @RequestBody AdminLoginPhoneBody adminLoginPhoneBody) {
        return userService.registryWithPhone(response, adminLoginPhoneBody);
    }

    /**
     * 用户分页查询接口
     *
     * @param params Object
     * @param sort Object
     * @param filter Object
     * @return Result
     */
    @GetMapping("user")
    public Result getList(String params, String sort, String filter, String page) {
        UserListParamsVo paramsBean = JSONUtil.toBean(params, UserListParamsVo.class);
        UserListSortVo sortBean = JSONUtil.toBean(sort, UserListSortVo.class);
        UserListFilterVo filterBean = JSONUtil.toBean(filter, UserListFilterVo.class);
        ListPageVo pageBean = JSONUtil.toBean(page, ListPageVo.class);
        ListParamsVo<UserListParamsVo, UserListSortVo, UserListFilterVo> userListParams = new ListParamsVo<>(paramsBean, sortBean, filterBean, pageBean);
        return userService.getList(userListParams);
    }
}
