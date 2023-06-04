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
import top.kkuily.xingbackend.model.dto.request.user.UserLoginAccountBodyDTO;
import top.kkuily.xingbackend.model.dto.request.user.UserLoginPhoneBodyDTO;
import top.kkuily.xingbackend.model.dto.response.ListResDTO;
import top.kkuily.xingbackend.model.dto.response.user.UserAuthInfoResDTO;
import top.kkuily.xingbackend.model.po.User;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListFilterVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListParamsVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListSortVO;
import top.kkuily.xingbackend.service.IUserService;
import top.kkuily.xingbackend.mapper.UserMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.utils.ErrorType;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.Token;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.kkuily.xingbackend.constant.commons.Api.PHONE_REG;
import static top.kkuily.xingbackend.constant.commons.Global.MAX_COUNT_PER_LIST;
import static top.kkuily.xingbackend.constant.user.Auth.*;

/**
 * @author 小K
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2023-05-18 11:15:44
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * @param response      HttpServletResponse
     * @param userLoginBody UserLoginAccountBodyDTO
     * @return Result
     * @description 用户登录服务
     */
    @Override
    public Result loginWithAccount(HttpServletResponse response, UserLoginAccountBodyDTO userLoginBody) {
        String username = userLoginBody.getUsername();
        String password = userLoginBody.getPassword();
        // 1. 判断账号是否为空
        if (username == null) {
            return Result.fail(400, "账号不能为空", ErrorType.NOTIFICATION);
        }
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("username", username);
        User userInfo = this.getOne(userWrapper);
        if (userInfo == null) {
            return Result.fail(400, "账号或密码错误，如果忘记密码，请使用手机号重置密码", ErrorType.NOTIFICATION);
        }
        // 2. 判断账号密码是否正确
        if (!userInfo.getUsername().equals(username) || !userInfo.getPassword().equals(password)) {
            return Result.fail(400, "账号或密码错误，如果忘记密码，请使用手机号重置密码", ErrorType.NOTIFICATION);
        }
        // 3. 生成Token
        String token = saveTokenVersion(userInfo, true, new DefaultClaims());
        // 4. 添加Token响应头
        response.addHeader(USER_TOKEN_KEY_IN_HEADER, token);
        // 5. 登录成功，返回用户基本信息
        return Result.success("登录成功", true);
    }

    /**
     * @param response            HttpServletResponse
     * @param userLoginPhoneBody UserLoginPhoneBodyDTO
     * @return Result
     * @description 用户使用手机号注册
     */
    @Override
    public Result registryWithPhone(HttpServletResponse response, UserLoginPhoneBodyDTO userLoginPhoneBody) {
        String phone = userLoginPhoneBody.getPhone();
        String sms = userLoginPhoneBody.getSms();
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
                    .get(USER_REGISTRY_CACHE_KEY + phone);
            if (sms.equals(smsInCache)) {
                // 根据手机号查询用户
                QueryWrapper<User> userWrapper = new QueryWrapper<>();
                userWrapper.eq("phone", phone);
                User user = this.getOne(userWrapper);
                if (user != null) {
                    return Result.fail(401, "手机号已存在，请返回登录", ErrorType.REDIRECT);
                }
                // 插入数据
                User userInsert = new User();
                userInsert.setUsername("xingz_cm_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8));
                userInsert.setPhone(phone);
                int isInsert = userMapper.insert(userInsert);
                if (isInsert == 1) {
                    // 生成token
                    String token = saveTokenVersion(userInsert, true, new DefaultClaims());
                    response.setHeader(USER_TOKEN_KEY_IN_HEADER, token);
                    return Result.success("注册成功", true);
                } else {
                    return Result.success("注册失败", false);
                }
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
     * @description 用户鉴权服务
     */
    @Override
    public Result auth(HttpServletRequest request) {
        // 1. 获取用户id
        String token = request.getHeader(USER_TOKEN_KEY_IN_HEADER);
        // 1.1 验证token是否有效
        Claims payload = Token.parse(token, USER_TOKEN_SECRET);
        if (payload == null) {
            return Result.fail(401, "无效Token", ErrorType.REDIRECT);
        }

        // 1.2 验证版本号是否有效
        String userId = payload.get("id").toString();
        String tokenVersion = payload.get("version").toString();
        String tokenKey = USER_TOKEN_VERSION_KEY + userId;
        String tokenVersionInCache = stringRedisTemplate.opsForValue().get(tokenKey);
        if (!tokenVersion.equals(tokenVersionInCache)) {
            return Result.fail(401, "令牌已失效，请重新登录", ErrorType.REDIRECT);
        }

        // 2. 查询数据库，验证用户身份
        // 2.1 查询用户角色
        User user = this.getById(userId);
        if (user == null) {
            return Result.fail(403, "禁止访问", ErrorType.NOTIFICATION);
        }

        // 2.2 脱敏（如：密码）
        UserAuthInfoResDTO userAuthInfoResDto = new UserAuthInfoResDTO();
        user.convertTo(userAuthInfoResDto);

        // 3. 返回数据
        return Result.success("验证成功", userAuthInfoResDto);
    }

    /**
     * @param userListParams ListParamsVO
     * @return Result
     * @description 分页查询
     * @author 小K
     */
    @Override
    public Result getList(ListParamsVO<UserListParamsVO, UserListSortVO, UserListFilterVO> userListParams) {
        // 1. 获取数据
        UserListParamsVO params = userListParams.getParams();
        UserListSortVO sort = userListParams.getSort();
        UserListFilterVO filter = userListParams.getFilter();
        ListPageVO page = userListParams.getPage();

        // 2. 将bean转化为map对象
        Map<String, Object> paramsMap = userListParams.getParams().beanToMapWithLimitField();

        // 3. 查询数据
        QueryWrapper<User> userListQuery = new QueryWrapper<>();
        log.info("birthday: {}", sort.getBirthday());
        userListQuery
                .allEq(paramsMap, false)
                .orderBy(true, "ascend".equals(sort.getCreatedTime()), "createdTime")
                .orderBy(true, "ascend".equals(sort.getModifiedTime()), "modifiedTime")
                .orderBy(true, "ascend".equals(sort.getBirthday()), "birthday");
        // 3.1 因为前端的小Bug，传递的数据有问题，在这里提前做判断，增强代码的健壮性
        if (filter.getGender() != null) {
            userListQuery.in(true, "gender", filter.getGender());
        }
        if (filter.getIsDeleted() != null) {
            userListQuery.in(true, "isDeleted", filter.getIsDeleted());
        }
        if (filter.getTagIds() != null) {
            userListQuery.in(true, "tagsId", filter.getTagIds());
        }
        if (filter.getIsVip() != null) {
            userListQuery.in(true, "isVip", filter.getIsVip());
        }
        // 3.2 因为前端的小Bug，传递的数据有问题，在这里提前做判断，增强代码的健壮性
        if (
                params.getCreatedTime() != null
                        &&
                        !("{".equals(params.getCreatedTime().getStartTime()))
                        &&
                        !("\"".equals(params.getCreatedTime().getEndTime()))
        ) {
            userListQuery
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
            userListQuery
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
        Page<User> userPageC = new Page<>(page.getCurrent(), page.getPageSize());
        // 4.1 查询未分页时的数据总数
        List<User> userNotPage = userMapper.selectList(userListQuery);
        // 4.2 查询分页后的数据
        Page<User> userPage = userMapper.selectPage(userPageC, userListQuery);

        log.info("current: {}", page.getCurrent());
        log.info("pageSize: {}", page.getPageSize());
        log.info("total: {}", userNotPage.size());
        log.info("users: {}", userPage.getRecords());

        // 5. 封装数据
        ListResDTO<User> userListRes = new ListResDTO<>();
        userListRes.setCurrent(page.getCurrent());
        userListRes.setPageSize(page.getPageSize());
        userListRes.setList(userPage.getRecords());
        userListRes.setTotal(userNotPage.size());
        return Result.success("获取成功", userListRes);
    }

    /**
     * @param userInfo User
     * @return String
     * @description 保存并将该token的版本号进行缓存
     */
    public String saveTokenVersion(User userInfo, Boolean isRegenerateVersion, Claims payload) {
        HashMap<String, Object> userInfoInToken = new HashMap<>();
        userInfoInToken.put("id", userInfo.getId());
        // 当前token版本号
        String tokenVersion = isRegenerateVersion ? UUID.randomUUID().toString() : payload.get("version").toString();
        userInfoInToken.put("version", tokenVersion);
        String token = Token.create(userInfoInToken, USER_TOKEN_SECRET);
        // token 版本号key，为了防止token还在有效期内，但是密码已经被修改的情况
        String tokenVersionKey = USER_TOKEN_VERSION_KEY + userInfo.getId();
        stringRedisTemplate.opsForValue()
                .set(tokenVersionKey, tokenVersion, USER_TOKEN_TTL, TimeUnit.MILLISECONDS);
        log.info("token:{}", token);
        log.info("tokenVersion:{}", tokenVersion);
        return token;
    }

}

