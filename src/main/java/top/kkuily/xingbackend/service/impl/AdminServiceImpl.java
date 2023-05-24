package top.kkuily.xingbackend.service.impl;

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
import top.kkuily.xingbackend.model.dto.request.admin.AdminAuthInfo;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginAccountBody;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginPhoneBody;
import top.kkuily.xingbackend.model.dto.request.commons.ListParams;
import top.kkuily.xingbackend.model.dto.response.admin.AdminListRes;
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
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.kkuily.xingbackend.constant.admin.Login.*;
import static top.kkuily.xingbackend.constant.commons.Api.PHONE_REG;

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
     * 管理员账号登录服务
     *
     * @param response              HttpServletResponse
     * @param adminLoginAccountBody AdminLoginAccountBody
     * @return Result
     */
    @Override
    public Result loginWithAccount(HttpServletResponse response, AdminLoginAccountBody adminLoginAccountBody) {
        String id = adminLoginAccountBody.getId();
        String password = adminLoginAccountBody.getPassword();
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
        response.addHeader(ADMIN_TOKEN_KEY_IN_HEADER, token);
        // 5. 登录成功，返回管理员基本信息
        return Result.success("登录成功", true);
    }

    /**
     * @param response            HttpServletResponse
     * @param adminLoginPhoneBody AdminLoginPhoneBody
     * @return Result
     * @description 使用手机号登录
     */
    @Override
    public Result loginWithPhone(HttpServletResponse response, AdminLoginPhoneBody adminLoginPhoneBody) {
        String phone = adminLoginPhoneBody.getPhone();
        String sms = adminLoginPhoneBody.getSms();
        if (phone == null) {
            return Result.fail(401, "非法请求", ErrorType.ERROR_MESSAGE);
        }
        if (sms == null) {
            return Result.fail(400, "验证码参数不能为空", ErrorType.NOTIFICATION);
        }
        Pattern reg = Pattern.compile(PHONE_REG);
        Matcher matcher = reg.matcher(phone);
        if (matcher.matches()) {
            String smsInCache = stringRedisTemplate.opsForValue()
                    .get(ADMIN_SMS_CHCHE_KEY + phone);
            if (sms.equals(smsInCache)) {
                // 根据手机号查询管理员
                QueryWrapper<Admin> adminWrapper = new QueryWrapper<>();
                adminWrapper.eq("phone", phone);
                Admin admin = this.getOne(adminWrapper);
                if (admin == null) {
                    return Result.fail(401, "手机号不存在，禁止访问", ErrorType.NOTIFICATION);
                }
                // 生成token
                String token = saveTokenVersion(admin, true, new DefaultClaims());
                response.setHeader(ADMIN_TOKEN_KEY_IN_HEADER, token);
                return Result.success("登录成功", true);
            } else {
                return Result.fail(403, "验证码错误，请重新输入", ErrorType.ERROR_MESSAGE);
            }
        } else {
            return Result.fail(403, "手机号格式错误，请检查手机号格式", ErrorType.NOTIFICATION);
        }
    }

    /**
     * @param request HttpServletRequest
     * @return Result
     * @description 管理员鉴权服务
     */
    @Override
    public Result auth(HttpServletRequest request) {
        // 1. 获取管理员id
        String token = request.getHeader(ADMIN_TOKEN_KEY_IN_HEADER);
        // 1.1 验证token是否有效
        Claims payload = Token.parse(token, ADMIN_TOKEN_SECRET);
        if (payload == null) {
            return Result.fail(401, "无效Token", ErrorType.REDIRECT);
        }

        // 1.2 验证版本号是否有效
        String adminId = payload.get("id").toString();
        String tokenVersion = payload.get("version").toString();
        String tokenKey = ADMIN_TOKEN_VERSION_KEY + adminId;
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
        if (role == null) {
            return Result.fail(500, "服务器内部错误，请联系超级管理员后再试", ErrorType.REDIRECT);
        }

        // 2.3 通过角色ID查询相应的权限ID进行权限整合
        String[] authList = StringUtils.split(role.getAuthList(), ",");
        List<String> authRoutes = new ArrayList<>();
        List<String> authSideBars = new ArrayList<>();
        if (authList != null) {
            for (String id : authList) {
                RoleAuth authInfo = roleAuthService.getById(id);
                authRoutes.add(authInfo.getAuthRoute());
                authSideBars.add(authInfo.getAuthSideBar());
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
        JSONObject params = JSONUtil.parseObj(listParams.getParams());
        JSONObject filter = JSONUtil.parseObj(listParams.getFilter());
        JSONObject sort = JSONUtil.parseObj(listParams.getSort());

        String current = params.get("current").toString();
        String pageSize = params.get("pageSize").toString();
        String id = params.get("id").toString();
        String name = params.get("name").toString();
        String phone = params.get("phone").toString();
        String createdTime = params.get("createdTime").toString();
        String modifiedTime = params.get("modifiedTime").toString();
        Object createdStartTime = JSONUtil.parseObj(createdTime).get("startTime");
        Object createdEndTime = JSONUtil.parseObj(createdTime).get("endTime");
        Object modifiedStartTime = JSONUtil.parseObj(modifiedTime).get("startTime");
        Object modifiedEndTime = JSONUtil.parseObj(modifiedTime).get("endTime");

        String roleId = filter.get("roleId").toString();
        String deptId = filter.get("deptId").toString();
        String gender = filter.get("gender").toString();
        String isDeleted = filter.get("isDeleted").toString();

        String sortCreatedTime = null;
        if (!sort.isNull("createdTime")) {
            sortCreatedTime = sort.get("createdTime").toString();
        }
        String sortModifiedTime = null;
        if (!sort.isNull("modifiedTime")) {
            sortModifiedTime = sort.get("modifiedTime").toString();
        }

        HashMap<String, String> listMap = new HashMap<>();
//        listMap.put("current", current);
//        listMap.put("pageSize", pageSize);
        listMap.put("id", id);
        listMap.put("phone", phone);
        listMap.put("roleId", roleId);
        listMap.put("deptId", deptId);
        listMap.put("gender", gender);
        listMap.put("isDeleted", isDeleted);
//        listMap.put("sortCreatedTime", sortCreatedTime);
//        listMap.put("sortModifiedTime", sortModifiedTime);

        QueryWrapper<Admin> listWrapper = new QueryWrapper<>();
        listWrapper
                .allEq(listMap, true)
                .like("name", name)
//                .between(true, "createdTime", createdStartTime, createdEndTime)
//                .between(true, "modifiedTime", modifiedStartTime, modifiedEndTime)
                .orderBy(true, "ascend".equals(sortCreatedTime), "createdTime")
                .orderBy(true, "ascend".equals(sortModifiedTime), "modifiedTime");
        List<Admin> list = this.list(listWrapper);
        AdminListRes adminListRes = new AdminListRes();
        adminListRes.setCurrent(Integer.parseInt(current));
        adminListRes.setPageSize(Integer.parseInt(pageSize));
        adminListRes.setList(list);
        adminListRes.setTotal(list.size());

        return Result.success("获取成功", adminListRes);
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
        String token = Token.create(adminInfoInToken, ADMIN_TOKEN_SECRET);
        // token 版本号key，为了防止token还在有效期内，但是密码已经被修改的情况
        String tokenVersionKey = ADMIN_TOKEN_VERSION_KEY + adminInfo.getId();
        stringRedisTemplate.opsForValue()
                .set(tokenVersionKey, tokenVersion, ADMIN_TOKEN_TTL, TimeUnit.MILLISECONDS);
        log.info("token:{}", token);
        log.info("tokenVersion:{}", tokenVersion);
        return token;
    }

}

