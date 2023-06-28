package top.kkuily.xingbackend.web.controller.admin;

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
import top.kkuily.xingbackend.model.enums.AuthEnums;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.model.dto.request.admin.AdminAddBodyDTO;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginAccountBodyDTO;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginPhoneBodyDTO;
import top.kkuily.xingbackend.model.dto.request.admin.AdminUpdateBodyDTO;
import top.kkuily.xingbackend.model.enums.IsDeletedEnums;
import top.kkuily.xingbackend.model.po.Admin;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListFilterVO;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListParamsVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListSortVO;
import top.kkuily.xingbackend.service.IAdminService;
import top.kkuily.xingbackend.service.other.SmsCaptchaService;
import top.kkuily.xingbackend.utils.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.kkuily.xingbackend.constant.admin.Auth.*;
import static top.kkuily.xingbackend.constant.commons.Pattern.PHONE_REG;
import static top.kkuily.xingbackend.constant.admin.AdminInfo.ADMIN_DEFAULT_ID_PREFIX;
import static top.kkuily.xingbackend.constant.admin.AdminInfo.ADMIN_DEFAULT_NAME_PREFIX;

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
    private SmsCaptchaService smsCaptchaService;

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
            return Result.fail(401, "非法请求", MsgType.ERROR_MESSAGE);
        }
        Pattern reg = Pattern.compile(PHONE_REG);
        Matcher matcher = reg.matcher(phone);
        if (!matcher.matches()) {
            return Result.fail(403, "手机号格式错误，请检查手机号格式", MsgType.NOTIFICATION);
        } else {
            int sms = smsCaptchaService.send(phone);
            // 存入redis中
            stringRedisTemplate.opsForValue().set(ADMIN_SMS_CACHE_KEY + phone, String.valueOf(sms), 3, TimeUnit.MINUTES);
            if (sms != 0) {
                return Result.success("发送成功，验证码有效期为三分钟", true);
            } else {
                return Result.fail(500, "服务器异常，请联系站长进行修复后再试", MsgType.NOTIFICATION);
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
    @GetMapping("admin")
    @Permission(authId = AuthEnums.ADMIN_LIST)
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
    @PostMapping("admin")
    @Permission(authId = AuthEnums.ADMIN_ADD)
    public Result add(@RequestBody AdminAddBodyDTO adminAddBodyDTO) {
        log.info("adminAddBodyDTO: {}", adminAddBodyDTO);
        try {
            // 1. 判空
            ValidateUtils.validateNotEmpty("角色", adminAddBodyDTO.getRoleId());
            ValidateUtils.validateNotEmpty("电话号", adminAddBodyDTO.getPhone());
            ValidateUtils.validateNotEmpty("部门", adminAddBodyDTO.getDeptId());
            // 2. 判长
            if (!StringUtils.isEmpty(adminAddBodyDTO.getId())) {
                ValidateUtils.validateLength("账号", adminAddBodyDTO.getId(), 10, 10);
            }
            ValidateUtils.validateLength("电话号", adminAddBodyDTO.getPhone(), 11, 11);
            // 3. 正则验证
            ValidateUtils.validateMobile("电话号", adminAddBodyDTO.getPhone());
            // 4. 判断账号是否符合规范
            Admin adminInTable;
            QueryWrapper<Admin> adminWrapper = new QueryWrapper<>();
            if (!StringUtils.isEmpty(adminAddBodyDTO.getId())) {
                adminWrapper
                        .eq(true, "id", adminAddBodyDTO.getId())
                        .or()
                        .eq("phone", adminAddBodyDTO.getPhone());
            } else {
                adminWrapper.eq("phone", adminAddBodyDTO.getPhone());
            }
            adminInTable = adminService.getOne(adminWrapper);
            if (adminInTable != null) {
                throw new IllegalArgumentException("账号已存在，请重新输入");
            }
        } catch (Exception e) {
            return Result.fail(403, e.getMessage(), MsgType.ERROR_MESSAGE);
        }
        Admin admin = new Admin();
        adminAddBodyDTO.convertTo(admin);
        String uuid = StringUtils.split(UUID.randomUUID(true).toString(), "-")[0];
        if (StringUtils.isEmpty(adminAddBodyDTO.getId())) {
            // 账号未输入，设置默认账号（ID）
            admin.setId(ADMIN_DEFAULT_ID_PREFIX + uuid);
        }
        if (StringUtils.isEmpty(adminAddBodyDTO.getName())) {
            // 名称未输入，设置默认管理员名称（name）
            admin.setName(ADMIN_DEFAULT_NAME_PREFIX + uuid);
        }
        if (StringUtils.isEmpty(adminAddBodyDTO.getPassword())) {
            // 密码未输入，设置默认管理员密码
            admin.setPassword(CipherUtils.md5(ADMIN_DEFAULT_PASSWORD));
        }
        synchronized (admin.getId().intern()) {
            try {
                adminService.save(admin);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalArgumentException("新增错误");
            }
        }
        return Result.success("新增成功", true);
    }

    /**
     * @param id String
     * @return Result
     * @description 删
     */
    @DeleteMapping("admin")
    @Permission(authId = AuthEnums.ADMIN_DEL)
    public Result del(String id, HttpServletRequest request) {
        // 1. 判断账号是否存在
        Admin adminInTable = adminService.getById(id);
        if (adminInTable == null) {
            return Result.fail(403, "账号不存在", MsgType.ERROR_MESSAGE);
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
            return Result.fail(400, "删除失败，无法删除自己", MsgType.ERROR_MESSAGE);
        }
        boolean isDel;
        synchronized (id.intern()) {
            isDel = adminService.removeById(id);
        }
        if (isDel) {
            return Result.success("删除成功", true);
        } else {
            return Result.fail(500, "删除失败，未知错误", MsgType.ERROR_MESSAGE);
        }
    }

    /**
     * @param adminUpdateBodyDTO AuthUpdateBodyDTO
     * @return Result
     * @description 改
     */
    @PutMapping("admin")
    @Permission(authId = AuthEnums.ADMIN_UPDATE)
    public Result update(@RequestBody AdminUpdateBodyDTO adminUpdateBodyDTO) {
        try {
            if (!StringUtils.isEmpty(adminUpdateBodyDTO.getId())) {
                ValidateUtils.validateLength("账号", adminUpdateBodyDTO.getId(), 0, 36);
            }
            if (!StringUtils.isEmpty(adminUpdateBodyDTO.getPhone())) {
                ValidateUtils.validateLength("电话号码", adminUpdateBodyDTO.getPhone(), 11, 11);
                // 正则验证
                ValidateUtils.validateMobile("电话号码", adminUpdateBodyDTO.getPhone());
            }
        } catch (Exception e) {
            return Result.fail(403, e.getMessage(), MsgType.ERROR_MESSAGE);
        }
        String id = adminUpdateBodyDTO.getId();
        try {
            ValidateUtils.validateNotEmpty("账号", id);
        } catch (Exception e) {
            return Result.fail(401, "参数异常", MsgType.ERROR_MESSAGE);
        }
        // 判断账号是否存在
        Admin adminInTable = adminService.getById(id);
        if (adminInTable == null) {
            return Result.fail(403, "账号不存在", MsgType.ERROR_MESSAGE);
        }
        synchronized (adminUpdateBodyDTO.getId().intern()) {
            Admin admin = new Admin();
            adminUpdateBodyDTO.convertTo(admin);
            log.error("error: {}", admin);
            boolean isDel = adminService.updateById(admin);
            if (isDel) {
                return Result.success("更新成功", true);
            } else {
                return Result.fail(403, "更新失败", MsgType.ERROR_MESSAGE);
            }
        }
    }

    /**
     * @param id String
     * @return Result
     * @description 获取某个管理员
     */
    @GetMapping("/admin/:id")
    @Permission(authId = AuthEnums.ADMIN_CHECK)
    public Result get(@PathParam("id") String id) {
        // 1. 判断账号是否存在
        Admin adminInTable = adminService.getById(id);
        if (adminInTable == null) {
            return Result.fail(403, "账号不存在", MsgType.ERROR_MESSAGE);
        }
        // 2. 判断是否被逻辑删除
        String isDeleted = adminInTable.getIsDeleted();
        if (IsDeletedEnums.NO.getValue().equals(isDeleted)) {
            return Result.success("获取成功", true);
        } else {
            return Result.success("获取成功，该管理员已被删除", true);
        }
    }

    // endregion
}
