package top.kkuily.xingbackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import top.kkuily.xingbackend.model.dto.request.AdminAuthInfo;
import top.kkuily.xingbackend.model.dto.request.ListParams;
import top.kkuily.xingbackend.model.dto.request.AdminLoginBody;
import top.kkuily.xingbackend.model.dto.response.AdminListRes;
import top.kkuily.xingbackend.model.po.Admin;
import top.kkuily.xingbackend.model.po.Role;
import top.kkuily.xingbackend.model.po.RoleAuth;
import top.kkuily.xingbackend.service.IAdminService;
import top.kkuily.xingbackend.service.IRoleAuthService;
import top.kkuily.xingbackend.service.IRoleService;
import top.kkuily.xingbackend.mapper.AdminMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.utils.ErrorType;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static top.kkuily.xingbackend.constant.Login.*;

/**
 * @author 小K
 * @description 针对表【admin】的数据库操作Service实现
 * @createDate 2023-05-18 11:15:44
 */
@Service
@Slf4j
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Resource
    private IRoleService roleService;

    @Resource
    private IRoleAuthService roleAuthService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 管理员登录服务
     *
     * @param response       HttpServletResponse
     * @param adminLoginBody AdminLoginBody
     * @return Result
     */
    @Override
    public Result login(HttpServletResponse response, AdminLoginBody adminLoginBody) {
        String type = adminLoginBody.getType();
        switch (type) {
            case "account": {
                return loginWithAccount(response, adminLoginBody);
            }
            case "mobile": {
                return loginWithMobile(response, adminLoginBody);
            }
            default: {
                return Result.fail(401, "非法请求", ErrorType.NOTIFICATION);
            }
        }
    }

    /**
     * 管理员鉴权服务
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @Override
    public Result auth(HttpServletRequest request) {
        // 1. 获取管理员id
        String token = request.getHeader(TOKEN_KEY_IN_HEADER);
        // 1.1 验证token是否有效
        Claims payload = Token.parse(token);
        if (payload == null) {
            return Result.fail(401, "无效Token", ErrorType.REDIRECT);
        }
        // 1.2 验证版本号是否有效
        String adminId = payload.get("id").toString();
        String tokenVersion = payload.get("version").toString();
        String tokenKey = TOKEN_VERSION_KEY + adminId;
        String tokenVersionInCache = stringRedisTemplate.opsForValue().get(tokenKey);
        if (!tokenVersion.equals(tokenVersionInCache)) {
            return Result.fail(401, "令牌已失效，请重新登录", ErrorType.REDIRECT);
        }

        // 2. 查询数据库，验证管理员身份
        // 2.1 查询管理员角色
        Admin admin = this.getById(adminId);
        if (admin == null) {
            return Result.fail(403, "禁止访问", ErrorType.NOTIFICATION);
        }

        // 2.2 通过管理员角色ID进行查询角色信息
        Role role = roleService.getById(admin.getRoleid());
        log.info("roleid: {}", admin.getRoleid());
        log.info("role: {}", role);
        log.info("list: {}", roleService.list());
        if (role == null) {
            return Result.fail(500, "服务器内部错误，请联系超级管理员后再试", ErrorType.REDIRECT);
        }

        // 2.3 通过角色ID查询相应的权限ID进行权限整合
        String[] authList = StringUtils.split(role.getAuthlist(), ",");
        List<String> authRoutes = new ArrayList<>();
        List<String> authSideBars = new ArrayList<>();
        if (authList != null) {
            for (String id : authList) {
                RoleAuth authInfo = roleAuthService.getById(id);
                authRoutes.add(authInfo.getAuthroute());
                authSideBars.add(authInfo.getAuthsidebar());
            }
        }

        // 3. 封装数据
        AdminAuthInfo adminAuthInfo = new AdminAuthInfo();
        adminAuthInfo.setAuthroutes(authRoutes);
        adminAuthInfo.setAuthsidebars(authSideBars);
        adminAuthInfo.setAdminAuthInfo(admin, role);

        // 4. 返回数据
        return Result.success("验证成功", adminAuthInfo);
    }

    /**
     * @param listParams AdminListParams
     * @return Result
     * @author 小K
     * @description 分页查询
     */
    @Override
    public Result getList(ListParams listParams) {
        // 1. 获取请求参数
        // gender, roleId, deptId, isDeleted
        JSONObject adminFilter = JSONUtil.parseObj(listParams.getFilter());
        // id, name, phone, createdTime, modifiedTime, current, pageSize
        JSONObject adminParams = JSONUtil.parseObj(listParams.getParams());
        // createdTime: "ascend", modifiedTime: "descend"
        JSONObject adminSort = JSONUtil.parseObj(listParams.getSort());

        // 2. 封装数据
        Admin admin = new Admin();
        admin.setGender((String) adminFilter.get("gender"));
        admin.setRoleid((String) adminFilter.get("roleId"));
        admin.setDeptid((String) adminFilter.get("deptId"));
        admin.setIsdeleted((String) adminFilter.get("isDeleted"));
        admin.setId((String) adminParams.get("id"));
        admin.setId((String) adminParams.get("name"));
        admin.setId((String) adminParams.get("phone"));

        // 3. TODO 查询数据
        Map<String, Object> adminWithMap = BeanUtil.beanToMap(admin);
        QueryWrapper<Admin> adminWrapper = new QueryWrapper<>();
        adminWrapper
                .allEq(adminWithMap, false)
                .orderBy(true, "ascend".equals(adminSort.get("createdTime")), "createdTime")
                .orderBy(true, "ascend".equals(adminSort.get("modifiedTime")), "modifiedTime");
        List<Admin> list = this.list(adminWrapper);
        // 4. TODO 将查询的数据进行进一步封装
        AdminListRes adminListRes = new AdminListRes();
        adminListRes.setList(list);
        adminListRes.setCurrent(Integer.parseInt((String) adminParams.get("current")));
        adminListRes.setTotal(list.size());
        adminListRes.setPageSize(Integer.parseInt((String) adminParams.get("pageSize")));
        // 5. TODO 返回数据
        return Result.success("请求成功", adminListRes);
    }

    /**
     * 使用账号密码登录
     *
     * @param response       HttpServletResponse
     * @param adminLoginBody AdminLoginBody
     * @return Result
     */
    private Result loginWithAccount(HttpServletResponse response, AdminLoginBody adminLoginBody) {
        String id = adminLoginBody.getId();
        String password = adminLoginBody.getPassword();
        // 1. 判断账号是否为空
        if (id == null) {
            return Result.fail(400, "账号不能为空", ErrorType.NOTIFICATION);
        }
        Admin adminInfo = this.getById(id);
        if (adminInfo == null) {
            return Result.fail(400, "账号或密码错误，如果忘记密码，请联系管理员重置密码", ErrorType.NOTIFICATION);
        }
        // 2. 判断账号密码是否正确
        if (!adminInfo.getId().equals(id) || !adminInfo.getPassword().equals(password)) {
            return Result.fail(400, "账号或密码错误，如果忘记密码，请联系管理员重置密码", ErrorType.NOTIFICATION);
        }
        // 3. 生成Token
        String token = saveTokenVersion(adminInfo, true, new DefaultClaims());
        // 4. 添加Token响应头
        response.addHeader(TOKEN_KEY_IN_HEADER, token);
        // 5. 登录成功，返回管理员基本信息
        return Result.success("登录成功", true);
    }

    /**
     * 使用手机号登录
     *
     * @param response       HttpServletResponse
     * @param adminLoginBody AdminLoginBody
     * @return Result
     */
    public Result loginWithMobile(HttpServletResponse response, AdminLoginBody adminLoginBody) {
        return null;
    }

    /**
     * 保存并将该token的版本号进行缓存
     *
     * @param adminInfo Admin
     * @return String
     */
    public String saveTokenVersion(Admin adminInfo, Boolean isRegenerateVersion, Claims payload) {
        HashMap<String, Object> adminInfoInToken = new HashMap<>();
        adminInfoInToken.put("id", adminInfo.getId());
        // 当前token版本号
        String tokenVersion = isRegenerateVersion ? UUID.randomUUID().toString() : payload.get("version").toString();
        adminInfoInToken.put("version", tokenVersion);
        String token = Token.create(adminInfoInToken);
        // token 版本号key，为了防止token还在有效期内，但是密码已经被修改的情况
        String tokenVersionKey = TOKEN_VERSION_KEY + adminInfo.getId();
        stringRedisTemplate.opsForValue()
                .set(tokenVersionKey, tokenVersion, TOKEN_TTL, TimeUnit.MILLISECONDS);
        log.info("token:{}", token);
        log.info("tokenVersion:{}", tokenVersion);
        return token;
    }

}

