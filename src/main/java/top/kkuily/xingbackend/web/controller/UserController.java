package top.kkuily.xingbackend.web.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.kkuily.xingbackend.anotation.Permission;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginPhoneBodyDTO;
import top.kkuily.xingbackend.model.dto.request.user.UserLoginAccountBodyDTO;
import top.kkuily.xingbackend.model.po.User;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListFilterVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListParamsVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListSortVO;
import top.kkuily.xingbackend.service.IUserService;
import top.kkuily.xingbackend.utils.ErrorType;
import top.kkuily.xingbackend.utils.PicCaptcha;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.SmsCaptcha;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.kkuily.xingbackend.constant.commons.Api.PHONE_REG;
import static top.kkuily.xingbackend.constant.user.Auth.USER_REGISTRY_CACHE_KEY;

/**
 * @author 小K
 * @description 用户相关接口
 */
@RestController
@Slf4j
public class UserController {
    @Resource
    private IUserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 账号登录接口
     *
     * @param response      HttpServletResponse
     * @param userLoginBody userLoginBody
     * @return Result
     */
    @PostMapping("user-login-account")
    public Result loginWithAccount(HttpServletResponse response, @RequestBody UserLoginAccountBodyDTO userLoginBody) {
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
     * @return Result
     * @description 获取图形验证码
     */
    @GetMapping("user-captcha")
    public Result getCaptcha() throws IOException {
        Map<String, String> codeInfo = PicCaptcha.generate();
        return Result.success("获取验证码成功", codeInfo);
    }

    /**
     * @param phone phone
     * @return Result
     * @description 发送验证码
     * @author 小K
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
            // 判断手机号是否已注册
            QueryWrapper<User> userWrapper = new QueryWrapper<>();
            userWrapper.eq("phone", phone);
            User user = userService.getOne(userWrapper);
            if (user != null) {
                return Result.fail(403, "这个手机号好像已被注册，请换个号试试吧", ErrorType.NOTIFICATION);
            }
            int sms = SmsCaptcha.send(phone);
            if (sms != 0) {
                // 缓存redis
                String isExist = stringRedisTemplate.opsForValue().get(USER_REGISTRY_CACHE_KEY + phone);
                if (isExist == null) {
                    stringRedisTemplate
                            .opsForValue()
                            .set(USER_REGISTRY_CACHE_KEY + phone, String.valueOf(sms), 3, TimeUnit.MINUTES);
                    return Result.success("发送成功，验证码有效期为三分钟", true);
                } else {
                    return Result.success("发送失败，请不要重复发送验证码", false);
                }
            } else {
                return Result.fail(500, "服务器异常，请联系站长进行修复后再试", ErrorType.NOTIFICATION);
            }
        }
    }

    /**
     * @param response            HttpServletResponse
     * @param adminLoginPhoneBody AdminLoginPhoneBodyDTO
     * @return Result
     * @description 用户手机号注册接口
     */
    @PostMapping("user-registry-phone")
    public Result loginWithPhone(HttpServletResponse response, @RequestBody AdminLoginPhoneBodyDTO adminLoginPhoneBody) {
        return userService.registryWithPhone(response, adminLoginPhoneBody);
    }

    /**
     * 用户分页查询接口
     *
     * @param params Object
     * @param sort   Object
     * @param filter Object
     * @return Result
     */
    @Permission(authId = 1000)
    @GetMapping("user")
    public Result getList(String params, String sort, String filter, String page) {
        UserListParamsVO paramsBean = JSONUtil.toBean(params, UserListParamsVO.class);
        UserListSortVO sortBean = JSONUtil.toBean(sort, UserListSortVO.class);
        UserListFilterVO filterBean = JSONUtil.toBean(filter, UserListFilterVO.class);
        ListPageVO pageBean = JSONUtil.toBean(page, ListPageVO.class);
        ListParamsVO<UserListParamsVO, UserListSortVO, UserListFilterVO> userListParams = new ListParamsVO<>(paramsBean, sortBean, filterBean, pageBean);
        return userService.getList(userListParams);
    }
}
