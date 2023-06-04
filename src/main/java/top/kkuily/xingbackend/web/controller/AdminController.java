package top.kkuily.xingbackend.web.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.kkuily.xingbackend.anotation.AdminAuthToken;
import top.kkuily.xingbackend.anotation.Permission;
import top.kkuily.xingbackend.model.dto.request.admin.AdminAddBodyDTO;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginAccountBodyDTO;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginPhoneBodyDTO;
import top.kkuily.xingbackend.model.dto.request.admin.AdminUpdateBodyDTO;
import top.kkuily.xingbackend.model.enums.IsDeleted;
import top.kkuily.xingbackend.model.po.Admin;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListFilterVO;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListParamsVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListSortVO;
import top.kkuily.xingbackend.service.IAdminService;
import top.kkuily.xingbackend.utils.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.kkuily.xingbackend.constant.admin.Auth.*;
import static top.kkuily.xingbackend.constant.commons.Api.PHONE_REG;
import static top.kkuily.xingbackend.constant.commons.Global.ADMIN_DEFAULT_ID_PREFIX;
import static top.kkuily.xingbackend.constant.commons.Global.ADMIN_DEFAULT_NAME_PREFIX;

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
            stringRedisTemplate.opsForValue().set(ADMIN_SMS_CACHE_KEY + phone, String.valueOf(sms), 3, TimeUnit.MINUTES);
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
    @AdminAuthToken
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
     * @param adminAddBodyDTO AuthAddBodyDTO
     * @return Result
     * @description 增
     */
    @Permission(authId = 1006)
    @PostMapping("admin")
    public Result add(@RequestBody AdminAddBodyDTO adminAddBodyDTO) {
        try {
            // 1. 判空
            ValidateUtils.validateNotEmpty("角色", adminAddBodyDTO.getRoleId());
            ValidateUtils.validateNotEmpty("电话号", adminAddBodyDTO.getPhone());
            ValidateUtils.validateNotEmpty("部门", adminAddBodyDTO.getDeptId());
            // 2. 判长
            if (!adminAddBodyDTO.getId().isEmpty()) {
                log.error("id: {}", adminAddBodyDTO.getId());
                ValidateUtils.validateLength("账号", adminAddBodyDTO.getId(), 10, 10);
            }
            ValidateUtils.validateLength("电话号", adminAddBodyDTO.getPhone(), 11, 11);
            // 3. 正则验证
            ValidateUtils.validateMobile("电话号", adminAddBodyDTO.getPhone());
        } catch (Exception e) {
            return Result.fail(403, e.getMessage(), ErrorType.ERROR_MESSAGE);
        }
        // 4. 判断账号是否存在
        if (adminAddBodyDTO.getId() != null) {
            QueryWrapper<Admin> adminWrapper = new QueryWrapper<>();
            adminWrapper
                    .eq("id", adminAddBodyDTO.getId())
                    .or()
                    .eq("phone", adminAddBodyDTO.getPhone());
            Admin adminInTable = adminService.getOne(adminWrapper);
            if (adminInTable != null) {
                return Result.fail(403, "账号已存在，请重新输入", ErrorType.ERROR_MESSAGE);
            }
        }
        Admin admin = new Admin();
        adminAddBodyDTO.convertTo(admin);
        String uuid = StringUtils.split(UUID.randomUUID(true).toString(), "-")[0];
        if (adminAddBodyDTO.getId() == null) {
            // 设置默认账号（ID）
            admin.setId(ADMIN_DEFAULT_ID_PREFIX + uuid);
        }
        if (adminAddBodyDTO.getName() == null) {
            // 设置默认管理员名称（name）
            admin.setName(ADMIN_DEFAULT_NAME_PREFIX + uuid);
        }
        if (adminAddBodyDTO.getPassword() == null) {
            // 设置默认管理员密码
            admin.setPassword(CipherUtils.md5(ADMIN_DEFAULT_PASSWORD));
        }
        adminService.save(admin);
        return Result.success("添加成功", true);
    }

    /**
     * @param id String
     * @return Result
     * @description 删
     */
    @Permission(authId = 1007)
    @DeleteMapping("admin")
    public Result del(String id, HttpServletRequest request) {
        // 1. 判断账号是否存在
        Admin adminInTable = adminService.getById(id);
        if (adminInTable == null) {
            return Result.fail(403, "账号不存在", ErrorType.ERROR_MESSAGE);
        }
        // 2. 判断是否是本人，禁止删除本人
        String token;
        try {
            token = request.getHeader(ADMIN_TOKEN_KEY_IN_HEADER);
        } catch (Exception e) {
            throw new RuntimeException("删除失败，请联系超级管理员进行解决");
        }
        Claims parseToken = Token.parse(token, ADMIN_TOKEN_SECRET);
        Object idInToken = parseToken.get("id");
        if (idInToken.equals(id)) {
            return Result.fail(400, "删除失败，无法删除自己", ErrorType.ERROR_MESSAGE);
        }
        boolean isDel = adminService.removeById(id);
        if (isDel) {
            return Result.success("删除成功", true);
        } else {
            return Result.fail(500, "删除失败，未知错误", ErrorType.ERROR_MESSAGE);
        }
    }

    /**
     * @param adminUpdateBodyDTO AuthUpdateBodyDTO
     * @return Result
     * @description 改
     */
    @Permission(authId = 1005)
    @PutMapping("admin")
    public Result update(@RequestBody AdminUpdateBodyDTO adminUpdateBodyDTO) {
        try {
            if (!adminUpdateBodyDTO.getId().isEmpty()) {
                log.error("id: {}", adminUpdateBodyDTO.getId());
                ValidateUtils.validateLength("账号", adminUpdateBodyDTO.getId(), 0, 36);
            }
            if (!StringUtils.isEmpty(adminUpdateBodyDTO.getPhone())) {
                ValidateUtils.validateLength("电话号码", adminUpdateBodyDTO.getPhone(), 11, 11);
                // 正则验证
                ValidateUtils.validateMobile("电话号码", adminUpdateBodyDTO.getPhone());
            }
        } catch (Exception e) {
            return Result.fail(403, e.getMessage(), ErrorType.ERROR_MESSAGE);
        }
        String id = adminUpdateBodyDTO.getId();
        try {
            ValidateUtils.validateNotEmpty("账号", id);
        } catch (Exception e) {
            return Result.fail(401, "参数异常", ErrorType.ERROR_MESSAGE);
        }
        // 判断账号是否存在
        Admin adminInTable = adminService.getById(id);
        if (adminInTable == null) {
            return Result.fail(403, "账号不存在", ErrorType.ERROR_MESSAGE);
        }
        Admin admin = new Admin();
        adminUpdateBodyDTO.convertTo(admin);
        log.error("error: {}", admin);
        boolean isDel = adminService.updateById(admin);
        if (isDel) {
            return Result.success("更新成功", true);
        } else {
            return Result.fail(403, "删除失败", ErrorType.ERROR_MESSAGE);
        }
    }

    /**
     * @param id String
     * @return Result
     * @description 获取某个管理员
     */
    @Permission(authId = 1004)
    @GetMapping("/admin/:id")
    public Result get(@PathParam("id") String id) {
        // 1. 判断账号是否存在
        Admin adminInTable = adminService.getById(id);
        if (adminInTable == null) {
            return Result.fail(403, "账号不存在", ErrorType.ERROR_MESSAGE);
        }
        // 2. 判断是否被逻辑删除
        String isDeleted = adminInTable.getIsDeleted();
        if (IsDeleted.NO.getValue().equals(isDeleted)) {
            return Result.success("获取成功", true);
        } else {
            return Result.success("获取成功，该管理员已被删除", true);
        }
    }

    // endregion
}
