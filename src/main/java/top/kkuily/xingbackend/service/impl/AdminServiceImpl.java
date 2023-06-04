package top.kkuily.xingbackend.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import top.kkuily.xingbackend.model.dto.request.admin.AdminAuthInfoDTO;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginAccountBodyDTO;
import top.kkuily.xingbackend.model.dto.response.ListResDTO;
import top.kkuily.xingbackend.model.dto.response.admin.AdminAuthInfoResDTO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginPhoneBodyDTO;
import top.kkuily.xingbackend.model.po.Admin;
import top.kkuily.xingbackend.model.po.Role;
import top.kkuily.xingbackend.model.po.Auth;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListFilterVO;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListParamsVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListSortVO;
import top.kkuily.xingbackend.service.IAdminService;
import top.kkuily.xingbackend.mapper.AuthMapper;
import top.kkuily.xingbackend.mapper.RoleMapper;
import top.kkuily.xingbackend.mapper.AdminMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.utils.ErrorType;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.Token;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.kkuily.xingbackend.constant.admin.Auth.*;
import static top.kkuily.xingbackend.constant.commons.Api.PHONE_REG;
import static top.kkuily.xingbackend.constant.commons.Global.MAX_COUNT_PER_LIST;

/**
 * @author 小K
 * @description 针对表【admin】的数据库操作Service实现
 * @createDate 2023-05-18 11:15:44
 */
@Service
@Slf4j
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private AuthMapper authMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * @param response              HttpServletResponse
     * @param adminLoginAccountBody AdminLoginAccountBodyDTO
     * @return Result
     * @description 管理员账号登录服务
     */
    @Override
    public Result loginWithAccount(HttpServletResponse response, AdminLoginAccountBodyDTO adminLoginAccountBody) {
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
     * @param adminLoginPhoneBody AdminLoginPhoneBodyDTO
     * @return Result
     * @description 使用手机号登录
     */
    @Override
    public Result loginWithPhone(HttpServletResponse response, AdminLoginPhoneBodyDTO adminLoginPhoneBody) {
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
                    .get(ADMIN_SMS_CACHE_KEY + phone);
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

        // 2.2 脱敏
        AdminAuthInfoResDTO adminAuthInfoResDto = new AdminAuthInfoResDTO();
        admin.convertTo(adminAuthInfoResDto);

        // 2.3 通过管理员角色ID进行查询角色信息
        Role role = roleMapper.selectById(admin.getRoleId());
        log.info("roleid: {}", admin.getRoleId());
        log.info("role: {}", role);
        if (role == null) {
            return Result.fail(500, "服务器内部错误，请联系超级管理员后再试", ErrorType.REDIRECT);
        }

        // 2.4 通过角色ID查询相应的权限ID进行权限整合
        String[] authList = StringUtils.split(role.getAuthList(), ",");
        List<String> authRoutes = new ArrayList<>();
        if (authList != null) {
            for (String id : authList) {
                Auth authInfo = authMapper.selectById(id);
                if (authInfo == null) {
                    continue;
                }
                authRoutes.add(authInfo.getAuthRoute());
            }
        }

        // 3. 封装数据
        AdminAuthInfoDTO adminAuthInfo = new AdminAuthInfoDTO();
        adminAuthInfo.setAuthRoutes(authRoutes);
        adminAuthInfo.setAdminAuthInfo(adminAuthInfoResDto, role);

        // 4. 返回数据
        return Result.success("验证成功", adminAuthInfo);
    }

    /**
     * @param adminListParams ListParamsVO
     * @return Result
     * @author 小K
     * @description 分页查询
     */
    @Override
    public Result getList(ListParamsVO<AdminListParamsVO, AdminListSortVO, AdminListFilterVO> adminListParams) {
        // 1. 获取数据
        AdminListParamsVO params = adminListParams.getParams();
        AdminListSortVO sort = adminListParams.getSort();
        AdminListFilterVO filter = adminListParams.getFilter();
        ListPageVO page = adminListParams.getPage();

        // 2. 将bean转化为map对象
        Map<String, Object> paramsMap = adminListParams.getParams().beanToMapWithLimitField();

        // 3. 查询数据
        QueryWrapper<Admin> adminListQuery = new QueryWrapper<>();
        adminListQuery
                .allEq(paramsMap, false)
                .orderBy(true, "ascend".equals(sort.getCreatedTime()), "createdTime")
                .orderBy(true, "ascend".equals(sort.getModifiedTime()), "modifiedTime");
        // 3.1 因为前端的小Bug，传递的数据有问题，在这里提前做判断，增强代码的健壮性
        if (filter.getGender() != null) {
            adminListQuery.in(true, "gender", filter.getGender());
        }
        if (filter.getRoleId() != null) {
            adminListQuery.in(true, "roleId", filter.getRoleId());
        }
        if (filter.getDeptId() != null) {
            adminListQuery.in(true, "deptId", filter.getDeptId());
        }
        if (filter.getIsDeleted() != null) {
            adminListQuery.in(true, "isDeleted", filter.getIsDeleted());
        }
        // 3.2 因为前端的小Bug，传递的数据有问题，在这里提前做判断，增强代码的健壮性
        if (
                params.getCreatedTime() != null
                        &&
                        !("{".equals(params.getCreatedTime().getStartTime()))
                        &&
                        !("\"".equals(params.getCreatedTime().getEndTime()))
        ) {
            adminListQuery
                    .between(
                            true,
                            "createdTime",
                            params.getCreatedTime().getStartTime(),
                            params.getCreatedTime().getEndTime()
                    );
        }
        if (
                params.getModifiedTime() != null
                        &&
                        !("{".equals(params.getModifiedTime().getStartTime()))
                        &&
                        !("\"".equals(params.getModifiedTime().getEndTime()))
        ) {
            adminListQuery
                    .between(
                            true,
                            "modifiedTime",
                            params.getModifiedTime().getStartTime(),
                            params.getModifiedTime().getEndTime()
                    );
        }

        // 附加：防爬虫
        if (page.getPageSize() >= MAX_COUNT_PER_LIST) {
            return Result.fail(403, "爬虫无所遁形，禁止访问", ErrorType.REDIRECT);
        }

        // 4. 分页查询
        Page<Admin> adminPageC = new Page<>(page.getCurrent(), page.getPageSize());
        // 4.1 查询未分页时的数据总数
        List<Admin> adminNotPage = adminMapper.selectList(adminListQuery);
        // 4.2 查询分页后的数据
        Page<Admin> adminPage = adminMapper.selectPage(adminPageC, adminListQuery);

        log.info("current: {}", page.getCurrent());
        log.info("pageSize: {}", page.getPageSize());
        log.info("total: {}", adminNotPage.size());
        log.info("admins: {}", adminPage.getRecords());

        // 5. 封装数据
        ListResDTO<Admin> adminListRes = new ListResDTO<>();
        adminListRes.setCurrent(page.getCurrent());
        adminListRes.setPageSize(page.getPageSize());
        adminListRes.setList(adminPage.getRecords());
        adminListRes.setTotal(adminNotPage.size());
        return Result.success("获取成功", adminListRes);
    }

    /**
     * @param adminInfo Admin
     * @return String
     * @description 保存并将该token的版本号进行缓存
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

