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
import top.kkuily.xingbackend.model.dto.request.admin.AdminAddBodyDTO;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginAccountBodyDTO;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginPhoneBodyDTO;
import top.kkuily.xingbackend.model.po.Admin;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListFilterVO;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListParamsVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListSortVO;
import top.kkuily.xingbackend.service.IAdminService;
import top.kkuily.xingbackend.utils.ErrorType;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.SmsCaptcha;
import top.kkuily.xingbackend.utils.ValidateUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.kkuily.xingbackend.constant.admin.Auth.ADMIN_SMS_CACHE_KEY;
import static top.kkuily.xingbackend.constant.commons.Api.PHONE_REG;
import static top.kkuily.xingbackend.constant.commons.global.ADMIN_NAME_PREFIX;

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
     * @param response              HttpServletResponse
     * @param adminLoginAccountBody AdminLoginAccountBodyDTO
     * @return Result
     * @description 管理员账号登录接口
     */
    @PostMapping("admin-login-account")
    public Result loginWithAccount(HttpServletResponse response, @RequestBody AdminLoginAccountBodyDTO adminLoginAccountBody) {
        return adminService.loginWithAccount(response, adminLoginAccountBody);
    }

    /**
     * @param response            HttpServletResponse
     * @param adminLoginPhoneBody AdminLoginPhoneBodyDTO
     * @return Result
     * @description 管理员手机号登录接口
     */
    @PostMapping("admin-login-phone")
    public Result loginWithPhone(HttpServletResponse response, @RequestBody AdminLoginPhoneBodyDTO adminLoginPhoneBody) {
        return adminService.loginWithPhone(response, adminLoginPhoneBody);
    }

    /**
     * @param phone phone
     * @return Result
     * @description 发送验证码
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
                    .set(ADMIN_SMS_CACHE_KEY + phone, String.valueOf(sms), 3, TimeUnit.MINUTES);
            if (sms != 0) {
                return Result.success("发送成功，验证码有效期为三分钟", true);
            } else {
                return Result.fail(500, "服务器异常，请联系站长进行修复后再试", ErrorType.NOTIFICATION);
            }
        }
    }

    /**
     * @param request HttpServletRequest
     * @return Result
     * @description 权限校验端口
     */
    @PostMapping("admin-auth")
    public Result auth(HttpServletRequest request) {
        return adminService.auth(request);
    }

    /**
     * @param params AdminListParamsVO
     * @param sort   AdminListSortVO
     * @return Result
     * @description 管理员分页查询接口
     */
    @Permission(authId = 1004)
    @GetMapping("admin")
    public Result getList(String params, String sort, String filter, String page) {
        log.info("page: {}", params);
        AdminListParamsVO paramsBean = JSONUtil.toBean(params, AdminListParamsVO.class);
        AdminListSortVO sortBean = JSONUtil.toBean(sort, AdminListSortVO.class);
        AdminListFilterVO filterBean = JSONUtil.toBean(filter, AdminListFilterVO.class);
        ListPageVO pageBean = JSONUtil.toBean(page, ListPageVO.class);
        ListParamsVO<AdminListParamsVO, AdminListSortVO, AdminListFilterVO> listParams = new ListParamsVO<>(paramsBean, sortBean, filterBean, pageBean);
        return adminService.getList(listParams);
    }

    // region
    // 增删改查

    /**
     * @description 增
     * @param adminAddBodyDTO AdminAddBodyDTO
     * @return Result
     */
    @Permission(authId = 1006)
    @PostMapping("admin")
    public Result add(@RequestBody AdminAddBodyDTO adminAddBodyDTO) {
        try {
            // 1. 判空
            ValidateUtils.validateNotEmpty("id", adminAddBodyDTO.getId());
            ValidateUtils.validateNotEmpty("password", adminAddBodyDTO.getPassword());
            ValidateUtils.validateNotEmpty("roleId", adminAddBodyDTO.getRoleId());
            ValidateUtils.validateNotEmpty("phone", adminAddBodyDTO.getPhone());
            ValidateUtils.validateNotEmpty("deptId", adminAddBodyDTO.getDeptId());
            // 2. 判长
            ValidateUtils.validateLength("id", adminAddBodyDTO.getId(), 10, 10);
            ValidateUtils.validateLength("password", adminAddBodyDTO.getPassword(), 8, 12);
            ValidateUtils.validateLength("phone", adminAddBodyDTO.getPhone(), 11, 11);
            // 3. 正则验证
            ValidateUtils.validateMobile("phone", adminAddBodyDTO.getPhone());
        } catch (Exception e) {
            return Result.fail(403, e.getMessage(), ErrorType.ERROR_MESSAGE);
        }
        // 4. 判断账号是否存在
        QueryWrapper<Admin> adminWrapper = new QueryWrapper<>();
        adminWrapper
                .eq("id", adminAddBodyDTO.getId())
                .or()
                .eq("phone", adminAddBodyDTO.getPhone());
        Admin adminInTable = adminService.getOne(adminWrapper);
        if (adminInTable != null) {
            return Result.fail(403, "账号已存在，请重新输入", ErrorType.ERROR_MESSAGE);
        }
        Admin admin = new Admin();
        adminAddBodyDTO.convertToAdmin(admin);
        admin.setName(ADMIN_NAME_PREFIX + adminAddBodyDTO.getId());
        adminService.save(admin);
        return Result.success("添加成功", true);
    }

    /**
     * @description 删
     * @param id String
     * @return Result
     */
    @Permission(authId = 1007)
    @DeleteMapping("admin")
    public Result del(String id) {
        // 判断账号是否存在
        Admin adminInTable = adminService.getById(id);
        if (adminInTable == null) {
            return Result.fail(403, "账号不存在", ErrorType.ERROR_MESSAGE);
        }
        boolean isDel = adminService.removeById(id);
        if (isDel) {
            return Result.success("删除成功", true);
        } else {
            return Result.fail(403, "删除失败", ErrorType.ERROR_MESSAGE);
        }
    }

    // endregion
}
