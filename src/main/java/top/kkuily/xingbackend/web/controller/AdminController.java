package top.kkuily.xingbackend.web.controller;

import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.kkuily.xingbackend.model.vo.ListParamsVo;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginAccountBody;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginPhoneBody;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListFilterVo;
import top.kkuily.xingbackend.model.vo.ListPageVo;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListParamsVo;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListSortVo;
import top.kkuily.xingbackend.service.IAdminService;
import top.kkuily.xingbackend.utils.ErrorType;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.SmsCaptcha;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.kkuily.xingbackend.constant.admin.Login.ADMIN_SMS_CHCHE_KEY;
import static top.kkuily.xingbackend.constant.commons.Api.PHONE_REG;

/**
 * @author 小K
 * @description 管理员相关接口
 */
@RestController
@Slf4j
public class AdminController {
    @Resource
    private IAdminService adminService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * @description 管理员账号登录接口
     *
     * @param response              HttpServletResponse
     * @param adminLoginAccountBody AdminLoginAccountBody
     * @return Result
     */
    @PostMapping("admin-login-account")
    public Result loginWithAccount(HttpServletResponse response, @RequestBody AdminLoginAccountBody adminLoginAccountBody) {
        return adminService.loginWithAccount(response, adminLoginAccountBody);
    }

    /**
     * @description 管理员手机号登录接口
     *
     * @param response            HttpServletResponse
     * @param adminLoginPhoneBody AdminLoginPhoneBody
     * @return Result
     */
    @PostMapping("admin-login-phone")
    public Result loginWithPhone(HttpServletResponse response, @RequestBody AdminLoginPhoneBody adminLoginPhoneBody) {
        return adminService.loginWithPhone(response, adminLoginPhoneBody);
    }

    /**
     * @description 发送验证码
     * @param phone phone
     * @return Result
     * @author 小K
     */
    @GetMapping("admin-sms")
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
            // 存入redis中
            stringRedisTemplate.opsForValue()
                    .set(ADMIN_SMS_CHCHE_KEY + phone, String.valueOf(sms), 3, TimeUnit.MINUTES);
            if (sms != 0) {
                return Result.success("发送成功，验证码有效期为三分钟", true);
            } else {
                return Result.fail(500, "服务器异常，请联系站长进行修复后再试", ErrorType.NOTIFICATION);
            }
        }
    }

    /**
     * @description 权限校验端口
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @PostMapping("admin-auth")
    public Result auth(HttpServletRequest request) {
        return adminService.auth(request);
    }

    /**
     * @description 管理员分页查询接口
     *
     * @param params AdminListParamsVo
     * @param sort   AdminListSortVo
     * @return Result
     */
    @GetMapping("admin")
    public Result getList(String params, String sort, String filter, String page) {
        log.info("page: {}", params);
        AdminListParamsVo paramsBean = JSONUtil.toBean(params, AdminListParamsVo.class);
        AdminListSortVo sortBean = JSONUtil.toBean(sort, AdminListSortVo.class);
        AdminListFilterVo filterBean = JSONUtil.toBean(filter, AdminListFilterVo.class);
        ListPageVo pageBean = JSONUtil.toBean(page, ListPageVo.class);
        ListParamsVo<AdminListParamsVo, AdminListSortVo, AdminListFilterVo> listParams = new ListParamsVo<>(paramsBean, sortBean, filterBean, pageBean);
        return adminService.getList(listParams);
    }
}
