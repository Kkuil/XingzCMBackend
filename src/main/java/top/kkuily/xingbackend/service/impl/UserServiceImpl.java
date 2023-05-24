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
import top.kkuily.xingbackend.model.dto.request.commons.ListParams;
import top.kkuily.xingbackend.model.dto.request.user.UserLoginAccountBody;
import top.kkuily.xingbackend.model.dto.request.user.UserLoginPhoneBody;
import top.kkuily.xingbackend.model.po.User;
import top.kkuily.xingbackend.service.IUserService;
import top.kkuily.xingbackend.mapper.UserMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.utils.ErrorType;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.Token;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.kkuily.xingbackend.constant.admin.Login.ADMIN_SMS_CHCHE_KEY;
import static top.kkuily.xingbackend.constant.admin.Login.ADMIN_TOKEN_KEY_IN_HEADER;
import static top.kkuily.xingbackend.constant.commons.Api.PHONE_REG;
import static top.kkuily.xingbackend.constant.user.Login.*;

/**
 * @author 小K
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2023-05-18 11:15:44
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用户登录服务
     *
     * @param response      HttpServletResponse
     * @param userLoginBody UserLoginAccountBody
     * @return Result
     */
    @Override
    public Result loginWithAccount(HttpServletResponse response, UserLoginAccountBody userLoginBody) {
        String username = userLoginBody.getUsername();
        String password = userLoginBody.getPassword();
        // 1. 判断账号是否为空
        if (username == null) {
            return Result.fail(400, "账号不能为空", ErrorType.NOTIFICATION);
        }
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.like("username", username);
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
     * 使用手机号登录
     *
     * @param response      HttpServletResponse
     * @param userLoginPhoneBody UserLoginPhoneBody
     * @return Result
     */
    public Result loginWithPhone(HttpServletResponse response, UserLoginPhoneBody userLoginPhoneBody) {
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
                    .get(ADMIN_SMS_CHCHE_KEY + phone);
            if (sms.equals(smsInCache)) {
                // 根据手机号查询用户
                QueryWrapper<User> userWrapper = new QueryWrapper<>();
                userWrapper.eq("phone", phone);
                User user = this.getOne(userWrapper);
                if (user == null) {
                    return Result.fail(401, "手机号不存在，禁止访问", ErrorType.NOTIFICATION);
                }
                // 生成token
                String token = saveTokenVersion(user, true, new DefaultClaims());
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
     * 用户鉴权服务
     *
     * @param request HttpServletRequest
     * @return Result
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

        // 3. 返回数据
        return Result.success("验证成功", user);
    }

    /**
     * @param listParams UserListParams
     * @return Result
     * @author 小K
     * @description 分页查询
     */
    @Override
    public Result getList(ListParams listParams) {
        return Result.success("请求成功");
    }

    /**
     * 保存并将该token的版本号进行缓存
     *
     * @param userInfo User
     * @return String
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

