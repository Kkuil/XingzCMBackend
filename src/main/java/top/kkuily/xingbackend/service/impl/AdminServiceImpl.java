package top.kkuily.xingbackend.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginAccountBodyDTO;
import top.kkuily.xingbackend.model.dto.response.ListResDTO;
import top.kkuily.xingbackend.model.dto.response.admin.AdminAuthInfoResDTO;
import top.kkuily.xingbackend.model.dto.response.admin.AdminInfoResDTO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.dto.request.admin.AdminLoginPhoneBodyDTO;
import top.kkuily.xingbackend.model.po.Admin;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListFilterVO;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListParamsVO;
import top.kkuily.xingbackend.model.vo.admin.list.AdminListSortVO;
import top.kkuily.xingbackend.service.IAdminService;
import top.kkuily.xingbackend.mapper.AdminMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.Token;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.kkuily.xingbackend.constant.admin.Auth.*;
import static top.kkuily.xingbackend.constant.commons.Pattern.PHONE_REG;

/**
 * @author 小K
 * @description 针对表【admin】的数据库操作Service实现
 * @createDate 2023-05-18 11:15:44
 */
@Service
@Slf4j
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Resource
    private AdminMapper adminMapper;

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
            return Result.fail(400, "账号不能为空", MsgType.NOTIFICATION);
        }
        Admin adminInfo = this.getById(id);
        if (adminInfo == null) {
            return Result.fail(400, "账号或密码错误，如果忘记密码，请联系管理员重置密码", MsgType.NOTIFICATION);
        }
        // 2. 判断账号密码是否正确
        if (!adminInfo.getId().equals(id) || !adminInfo.getPassword().equals(password)) {
            return Result.fail(400, "账号或密码错误，如果忘记密码，请联系管理员重置密码", MsgType.NOTIFICATION);
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
            return Result.fail(401, "非法请求", MsgType.ERROR_MESSAGE);
        }
        if (sms == null) {
            return Result.fail(400, "验证码参数不能为空", MsgType.NOTIFICATION);
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
                    return Result.fail(401, "手机号不存在，禁止访问", MsgType.NOTIFICATION);
                }
                // 生成token
                String token = saveTokenVersion(admin, true, new DefaultClaims());
                response.setHeader(ADMIN_TOKEN_KEY_IN_HEADER, token);
                return Result.success("登录成功", true);
            } else {
                return Result.fail(403, "验证码错误，请重新输入", MsgType.ERROR_MESSAGE);
            }
        } else {
            return Result.fail(403, "手机号格式错误，请检查手机号格式", MsgType.NOTIFICATION);
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
        Claims payload;
        try {
            payload = Token.parse(token, ADMIN_TOKEN_SECRET);
        } catch (Exception e) {
            throw new IllegalArgumentException("无效Token");
        }

        // 1.2 验证版本号是否有效
        String adminId = payload.get("id").toString();
        String tokenVersion = payload.get("version").toString();
        String tokenKey = ADMIN_TOKEN_VERSION_KEY + adminId;
        String tokenVersionInCache = stringRedisTemplate.opsForValue().get(tokenKey);
        if (!tokenVersion.equals(tokenVersionInCache)) {
            return Result.fail(401, "令牌已失效，请重新登录", MsgType.REDIRECT);
        }

        AdminAuthInfoResDTO authInfo;
        try {
            authInfo = adminMapper.selectAuthInfo(adminId);
        } catch (Exception e) {
            return Result.fail(403, "Access denied.", MsgType.ERROR_MESSAGE);
        }

        // 4. 返回数据
        return Result.success("验证成功", authInfo);
    }

    /**
     * @param adminListParams ListParamsVO
     * @return Result
     * @author 小K
     * @description 分页查询
     */
    @Override
    public Result getList(ListParamsVO<AdminListParamsVO, AdminListSortVO, AdminListFilterVO> adminListParams) {
        // 获取数据
        AdminListParamsVO params = adminListParams.getParams();
        AdminListSortVO sort = adminListParams.getSort();
        AdminListFilterVO filter = adminListParams.getFilter();
        ListPageVO page = adminListParams.getPage();

        // 分页数据处理
        ListPageVO listPageVO = new ListPageVO();
        listPageVO.setCurrent((page.getCurrent() - 1) * page.getPageSize());
        listPageVO.setPageSize(page.getPageSize());
        adminListParams.setPage(listPageVO);

        // 查询数据
        List<AdminInfoResDTO> articleInfoResDTO = null;
        // 总条数
        int total = 0;
        try {
            articleInfoResDTO = adminMapper.listAdminsWithLimit(params, sort, filter, listPageVO);
            total = adminMapper.listAdminsWithNotLimit(params, sort, filter, listPageVO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 封装数据
        ListResDTO<AdminInfoResDTO> adminListRes = new ListResDTO<>();
        adminListRes.setCurrent(page.getCurrent());
        adminListRes.setPageSize(page.getPageSize());
        adminListRes.setList(articleInfoResDTO);
        adminListRes.setTotal(total);

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
        String tokenVersion;
        if (isRegenerateVersion) {
            tokenVersion = UUID.randomUUID().toString();
            adminInfoInToken.put("version", tokenVersion);
        } else {
            tokenVersion = payload.get("version").toString();
        }
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

