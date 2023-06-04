package top.kkuily.xingbackend.web.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.kkuily.xingbackend.anotation.Permission;
import top.kkuily.xingbackend.anotation.UserAuthToken;
import top.kkuily.xingbackend.model.dto.request.user.UserAddBodyDTO;
import top.kkuily.xingbackend.model.dto.request.user.UserLoginPhoneBodyDTO;
import top.kkuily.xingbackend.model.dto.request.user.UserUpdateBodyDTO;
import top.kkuily.xingbackend.model.dto.request.user.UserLoginAccountBodyDTO;
import top.kkuily.xingbackend.model.enums.IsDeleted;
import top.kkuily.xingbackend.model.po.User;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.user.UserByUsernameForChat;
import top.kkuily.xingbackend.model.vo.user.list.UserListFilterVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListParamsVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListSortVO;
import top.kkuily.xingbackend.service.IUserService;
import top.kkuily.xingbackend.utils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.kkuily.xingbackend.constant.commons.Api.PHONE_REG;
import static top.kkuily.xingbackend.constant.commons.Global.USER_DEFAULT_NAME_PREFIX;
import static top.kkuily.xingbackend.constant.user.Auth.USER_DEFAULT_PASSWORD;
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
    @UserAuthToken
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
     * @param response           HttpServletResponse
     * @param userLoginPhoneBody UserLoginPhoneBodyDTO
     * @return Result
     * @description 用户手机号注册接口
     */
    @PostMapping("user-registry-phone")
    public Result loginWithPhone(HttpServletResponse response, @RequestBody UserLoginPhoneBodyDTO userLoginPhoneBody) {
        return userService.registryWithPhone(response, userLoginPhoneBody);
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

    // region
    // 增删改查

    /**
     * @param userAddBodyDTO UserAddBodyDTO
     * @return Result
     * @description 增
     */
    @Permission(authId = 1002)
    @PostMapping("user")
    public Result add(@RequestBody UserAddBodyDTO userAddBodyDTO) {
        try {
            ValidateUtils.validateNotEmpty("电话号", userAddBodyDTO.getPhone());
            ValidateUtils.validateLength("电话号", userAddBodyDTO.getPhone(), 11, 11);
            ValidateUtils.validateMobile("电话号", userAddBodyDTO.getPhone());
            if (userAddBodyDTO.getUsername() != null) {
                ValidateUtils.validateLength("username", userAddBodyDTO.getUsername(), 8, 17);
            }
        } catch (Exception e) {
            return Result.fail(403, e.getMessage(), ErrorType.ERROR_MESSAGE);
        }
        // 判断用户名称是否已被占用
        String username = userAddBodyDTO.getUsername();
        if (username != null) {
            QueryWrapper<User> userWrapper = new QueryWrapper<>();
            userWrapper.eq("username", username);
            User userInTable = userService.getOne(userWrapper);
            if (userInTable != null) {
                return Result.fail(401, "该用户名已被占用", ErrorType.NOTIFICATION);
            }
        }
        String id = StringUtils.split(UUID.randomUUID(true).toString(), "-")[0];
        User user = new User();
        userAddBodyDTO.convertTo(user);
        user.setUsername(USER_DEFAULT_NAME_PREFIX + id);
        if (userAddBodyDTO.getPassword() == null) {
            user.setPassword(CipherUtils.md5(USER_DEFAULT_PASSWORD));
        }
        userService.save(user);
        return Result.success("添加成功", true);
    }

    /**
     * @param id String
     * @return Result
     * @description 删
     */
    @Permission(authId = 1003)
    @DeleteMapping("user")
    public Result del(String id) {
        // 1. 判断账号是否存在
        User userInTable = userService.getById(id);
        if (userInTable == null) {
            return Result.fail(403, "账号不存在", ErrorType.ERROR_MESSAGE);
        }
        // 2. 判断是否被逻辑删除
        String isDeleted = userInTable.getIsDeleted();
        if (IsDeleted.YES.getValue().equals(isDeleted)) {
            return Result.fail(400, "删除失败", ErrorType.ERROR_MESSAGE);
        }
        boolean isDel = userService.removeById(id);
        if (isDel) {
            return Result.success("删除成功", true);
        } else {
            return Result.fail(403, "删除失败", ErrorType.ERROR_MESSAGE);
        }
    }

    /**
     * @param userUpdateBodyDTO UserUpdateBodyDTO
     * @return Result
     * @description 改
     */
    @Permission(authId = 1001)
    @PutMapping("user")
    public Result update(@RequestBody UserUpdateBodyDTO userUpdateBodyDTO) {
        try {
            ValidateUtils.validateNotEmpty("电话号", userUpdateBodyDTO.getPhone());
            ValidateUtils.validateLength("电话号", userUpdateBodyDTO.getPhone(), 11, 11);
            ValidateUtils.validateMobile("电话号", userUpdateBodyDTO.getPhone());
            if (userUpdateBodyDTO.getUsername() != null) {
                ValidateUtils.validateLength("username", userUpdateBodyDTO.getUsername(), 8, 17);
            }
        } catch (Exception e) {
            return Result.fail(403, e.getMessage(), ErrorType.ERROR_MESSAGE);
        }
        // 判断账号是否存在
        User userInTable = userService.getById(userUpdateBodyDTO.getId());
        if (userInTable == null) {
            return Result.fail(403, "账号不存在", ErrorType.ERROR_MESSAGE);
        }
        User user = new User();
        userUpdateBodyDTO.convertTo(user);
        boolean isDel = userService.updateById(user);
        if (isDel) {
            return Result.success("更新成功", true);
        } else {
            return Result.fail(403, "删除失败", ErrorType.ERROR_MESSAGE);
        }
    }

    /**
     * @param username String
     * @return Result
     * @description 通过用户名获取用户
     */
    @GetMapping("/user/{username}")
    public Result getByUsername(@PathVariable String username) {
        // 判断账号是否存在
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.like("username", username);
        List<User> listUser = userService.list(userWrapper);
        if (listUser.size() == 0) {
            return Result.fail(403, "用户不存在", ErrorType.ERROR_MESSAGE);
        }
        List<UserByUsernameForChat> listChatInfo = new ArrayList<>(listUser.size());
        UserByUsernameForChat.userListConvertToChatList(listUser, listChatInfo);
        return Result.success("查询成功", listChatInfo);
    }

    // endregion
}
