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
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.AccessException;
import top.kkuily.xingbackend.mapper.UserRankMapper;
import top.kkuily.xingbackend.mapper.UserTagMapper;
import top.kkuily.xingbackend.model.dto.request.user.UserLoginAccountBodyDTO;
import top.kkuily.xingbackend.model.dto.request.user.UserLoginPhoneBodyDTO;
import top.kkuily.xingbackend.model.dto.response.ListResDTO;
import top.kkuily.xingbackend.model.dto.response.user.UserAuthInfoResDTO;
import top.kkuily.xingbackend.model.dto.response.user.UserInfoResDTO;
import top.kkuily.xingbackend.model.dto.response.user.UserInfoWithCenterResDTO;
import top.kkuily.xingbackend.model.po.User;
import top.kkuily.xingbackend.model.po.UserRank;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListFilterVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListParamsVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListSortVO;
import top.kkuily.xingbackend.service.IUserService;
import top.kkuily.xingbackend.mapper.UserMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.utils.CipherUtils;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.Token;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.kkuily.xingbackend.constant.commons.Pattern.PHONE_REG;
import static top.kkuily.xingbackend.constant.user.Auth.*;
import static top.kkuily.xingbackend.constant.user.UserInfo.USER_DEFAULT_NAME_PREFIX;

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
    private UserTagMapper userTagMapper;

    @Resource
    private UserRankMapper userRankMapper;

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
            return Result.fail(400, "账号不能为空", MsgType.NOTIFICATION);
        }
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("username", username);
        User userInfo = this.getOne(userWrapper);
        if (userInfo == null) {
            return Result.fail(400, "账号或密码错误，如果忘记密码，请使用手机号重置密码", MsgType.ERROR_MESSAGE);
        }
        // 2. 判断账号密码是否正确
        if (!userInfo.getUsername().equals(username) || !userInfo.getPassword().equals(password)) {
            return Result.fail(400, "账号或密码错误，如果忘记密码，请使用手机号重置密码", MsgType.ERROR_MESSAGE);
        }
        // 3. 生成Token
        String token = saveTokenVersion(userInfo, true, new DefaultClaims());
        // 4. 添加Token响应头
        response.addHeader(USER_TOKEN_KEY_IN_HEADER, token);
        // 5. 登录成功，返回用户基本信息
        return Result.success("登录成功", true, MsgType.REDIRECT);
    }

    /**
     * @param response           HttpServletResponse
     * @param userLoginPhoneBody UserLoginPhoneBodyDTO
     * @return Result
     * @description 用户使用手机号注册
     */
    @Override
    public Result loginRegistryWithPhone(HttpServletResponse response, UserLoginPhoneBodyDTO userLoginPhoneBody) {
        String phone = userLoginPhoneBody.getPhone();
        String sms = userLoginPhoneBody.getSms();
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
                    .get(USER_REGISTRY_CACHE_KEY + phone);
            if (sms.equals(smsInCache)) {
                // 根据手机号查询用户
                QueryWrapper<User> userWrapper = new QueryWrapper<>();
                userWrapper.eq("phone", phone);
                User user = this.getOne(userWrapper);
                if (user != null) {
                    // 1. 登录
                    // 生成token
                    String token = saveTokenVersion(user, true, new DefaultClaims());
                    response.setHeader(USER_TOKEN_KEY_IN_HEADER, token);
                    return Result.success("登录成功", true, MsgType.NOTIFICATION);
                } else {
                    // 2. 注册
                    // 插入数据
                    User userInsert = new User();
                    String uuid = UUID.randomUUID().toString().replace("-", "");
                    // 生成随机ID
                    userInsert.setId(uuid);
                    // 设置默认用户名
                    userInsert.setUsername(USER_DEFAULT_NAME_PREFIX + uuid.substring(0, 8));
                    // 设置手机号
                    userInsert.setPhone(phone);
                    // 设置默认密码
                    userInsert.setPassword(CipherUtils.md5(USER_DEFAULT_PASSWORD));
                    // 保存用户
                    int isInsertUser = userMapper.insert(userInsert);
                    // 保存用户等级
                    UserRank userRank = new UserRank();
                    int isInsertUserRank = userRankMapper.insert(userRank);
                    if (isInsertUser == 1 && isInsertUserRank == 1) {
                        // 生成token
                        // 获取用户id
                        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                        userQueryWrapper.eq("phone", phone);
                        User newUser = userMapper.selectOne(userQueryWrapper);
                        String token = saveTokenVersion(newUser, true, new DefaultClaims());
                        response.setHeader(USER_TOKEN_KEY_IN_HEADER, token);
                        return Result.success("注册成功", true, MsgType.NOTIFICATION);
                    } else {
                        return Result.success("注册失败", false, MsgType.ERROR_MESSAGE);
                    }
                }
            } else {
                return Result.fail(403, "验证码错误，请重新输入", MsgType.ERROR_MESSAGE);
            }
        } else {
            return Result.fail(403, "手机号格式错误，请检查手机号格式", MsgType.ERROR_MESSAGE);
        }
    }

    /**
     * @param request HttpServletRequest
     * @return Result
     * @description 用户鉴权服务
     */
    @Override
    public Result auth(HttpServletRequest request) throws AccessException {
        // 1. 获取用户id
        String token = request.getHeader(USER_TOKEN_KEY_IN_HEADER);
        // 1.1 判断是否有token
        if (StringUtils.isEmpty(token)) {
            throw new AccessException("空令牌");
        }

        // 1.2 验证token是否有效
        Claims payload;
        try {
            payload = Token.parse(token, USER_TOKEN_SECRET);
        } catch (Exception e) {
            throw new IllegalArgumentException("无效令牌");
        }

        // 1.3 验证版本号是否有效
        String userId = payload.get("id").toString();
        String tokenVersion = payload.get("version").toString();
        String tokenKey = USER_TOKEN_VERSION_KEY + userId;
        String tokenVersionInCache = stringRedisTemplate.opsForValue().get(tokenKey);
        if (!tokenVersion.equals(tokenVersionInCache)) {
            return Result.fail(401, "令牌已失效，请重新登录", MsgType.REDIRECT);
        }

        // 2. 查询数据库，验证用户身份
        // 2.1 查询用户角色
        User user = this.getById(userId);
        if (user == null) {
            return Result.fail(403, "禁止访问", MsgType.NOTIFICATION);
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
        // 获取数据
        UserListParamsVO params = userListParams.getParams();
        UserListSortVO sort = userListParams.getSort();
        UserListFilterVO filter = userListParams.getFilter();
        ListPageVO page = userListParams.getPage();

        // 分页数据处理
        ListPageVO listPageVO = new ListPageVO();
        listPageVO.setCurrent((page.getCurrent() - 1) * page.getPageSize());
        listPageVO.setPageSize(page.getPageSize());
        userListParams.setPage(listPageVO);

        // 查询数据
        List<UserInfoResDTO> userInfoResDTO = null;
        // 总条数
        int total = 0;
        try {
            userInfoResDTO = userMapper.listUsersWithLimit(params, sort, filter, listPageVO);
            for (int i = 0; i < userInfoResDTO.size(); i++) {
                String userId = userInfoResDTO.get(i).getId();
                List<Integer> tagIds = userTagMapper.selectTagIdsById(userId);
                userInfoResDTO.get(i).setTagIds(tagIds.toString());
            }
            total = userMapper.listUsersWithNotLimit(params, sort, filter, listPageVO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 封装数据
        ListResDTO<UserInfoResDTO> userListRes = new ListResDTO<>();
        userListRes.setCurrent(page.getCurrent());
        userListRes.setPageSize(page.getPageSize());
        userListRes.setList(userInfoResDTO);
        userListRes.setTotal(total);

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
        String tokenVersion;
        if (isRegenerateVersion) {
            tokenVersion = UUID.randomUUID().toString();
            userInfoInToken.put("version", tokenVersion);
        } else {
            tokenVersion = payload.get("version").toString();
        }
        String token = Token.create(userInfoInToken, USER_TOKEN_SECRET);
        // token 版本号key，为了防止token还在有效期内，但是密码已经被修改或别人异地登录的情况
        String tokenVersionKey = USER_TOKEN_VERSION_KEY + userInfo.getId();
        stringRedisTemplate.opsForValue()
                .set(tokenVersionKey, tokenVersion, USER_TOKEN_TTL, TimeUnit.MILLISECONDS);
        log.info("token:{}", token);
        log.info("tokenVersion:{}", tokenVersion);
        return token;
    }

    /**
     * @param id      String
     * @param request HttpServletRequest
     * @return Result
     * @description 通过用户ID获取用户信息
     */
    @Override
    public Result get(String id, HttpServletRequest request) {
        // 1. 获取Token
        String token = request.getHeader(USER_TOKEN_KEY_IN_HEADER);
        // 1.1 判断是否已登录
        if (StringUtils.isEmpty(token)) {
            UserInfoWithCenterResDTO user = userMapper.getUserById(id, "");
            return Result.success("获取成功", user, MsgType.SILENT);
        } else {
            // 1.2 解析Token
            Claims payload;
            try {
                payload = Token.parse(token, USER_TOKEN_SECRET);
                String userId = (String) payload.get("id");
                UserInfoWithCenterResDTO user = userMapper.getUserById(id, userId);
                return Result.success("获取成功", user, MsgType.SILENT);
            } catch (Exception e) {
                log.info("无效Token");
                UserInfoWithCenterResDTO user = userMapper.getUserById(id, "");
                return Result.success("获取成功", user, MsgType.SILENT);
            }
        }
    }
}

