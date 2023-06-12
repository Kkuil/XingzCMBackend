package top.kkuily.xingbackend.web.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.kkuily.xingbackend.anotation.ApiSignAuth;
import top.kkuily.xingbackend.anotation.UserAuthToken;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.model.dto.request.user.UserLoginAccountBodyDTO;
import top.kkuily.xingbackend.model.dto.request.user.UserLoginPhoneBodyDTO;
import top.kkuily.xingbackend.model.po.User;
import top.kkuily.xingbackend.service.IUserService;
import top.kkuily.xingbackend.service.other.SmsCaptchaService;
import top.kkuily.xingbackend.utils.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.kkuily.xingbackend.constant.commons.Pattern.PHONE_REG;
import static top.kkuily.xingbackend.constant.user.Auth.USER_REGISTRY_CACHE_KEY;

/**
 * @author 小K
 * @description 用户相关接口
 */
@RestController
@Slf4j
public class UUserController {
    @Resource
    private IUserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SmsCaptchaService smsCaptchaService;

    /**
     * 账号登录接口
     *
     * @param response      HttpServletResponse
     * @param userLoginBody userLoginBody
     * @return Result
     */
    @PostMapping("user-login-account")
    @ApiSignAuth
    public Result loginWithAccount(HttpServletResponse response, @RequestBody UserLoginAccountBodyDTO userLoginBody) {
        return userService.loginWithAccount(response, userLoginBody);
    }

    /**
     * 权限校验端口
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @PostMapping("user-auth")
    @UserAuthToken
    @ApiSignAuth
    public Result auth(HttpServletRequest request) {
        return userService.auth(request);
    }

    /**
     * @return Result
     * @description 获取图形验证码
     */
    @GetMapping("user-captcha")
    @ApiSignAuth
    public Result getCaptcha() throws IOException {
        Map<String, String> codeInfo = PicCaptcha.generate();
        return Result.success("获取验证码成功", codeInfo);
    }

    /**
     * @param phone phone
     * @return Result
     * @description 发送验证码
     * @author 小K
     */
    @GetMapping("user-sms")
    @ApiSignAuth
    public Result getSmsCaptcha(String phone) throws ExecutionException, InterruptedException {
        if (phone == null) {
            return Result.fail(401, "非法请求", MsgType.ERROR_MESSAGE);
        }
        Pattern reg = Pattern.compile(PHONE_REG);
        Matcher matcher = reg.matcher(phone);
        if (!matcher.matches()) {
            return Result.fail(403, "手机号格式错误，请检查手机号格式", MsgType.NOTIFICATION);
        } else {
            // 判断手机号是否已注册
            QueryWrapper<User> userWrapper = new QueryWrapper<>();
            userWrapper.eq("phone", phone);
            User user = userService.getOne(userWrapper);
            if (user != null) {
                return Result.fail(403, "这个手机号好像已被注册，请换个号试试吧", MsgType.NOTIFICATION);
            }
            int sms = smsCaptchaService.send(phone);
            if (sms != 0) {
                // 缓存redis
                String isExist = stringRedisTemplate.opsForValue().get(USER_REGISTRY_CACHE_KEY + phone);
                if (isExist == null) {
                    stringRedisTemplate.opsForValue().set(USER_REGISTRY_CACHE_KEY + phone, String.valueOf(sms), 3, TimeUnit.MINUTES);
                    return Result.success("发送成功，验证码有效期为三分钟", true);
                } else {
                    return Result.success("发送失败，请不要重复发送验证码", false);
                }
            } else {
                return Result.fail(500, "服务器异常，请联系站长进行修复后再试", MsgType.NOTIFICATION);
            }
        }
    }

    /**
     * @param response           HttpServletResponse
     * @param userLoginPhoneBody UserLoginPhoneBodyDTO
     * @return Result
     * @description 用户手机号注册接口
     */
    @PostMapping("user-registry-phone")
    @ApiSignAuth
    public Result loginWithPhone(HttpServletResponse response, @RequestBody UserLoginPhoneBodyDTO userLoginPhoneBody) {
        return userService.registryWithPhone(response, userLoginPhoneBody);
    }
}
