package top.kkuily.xingbackend.web.controller.upload.user;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.kkuily.xingbackend.anotation.UserAuthToken;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.service.other.AliyunOSSService;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.Token;

import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_KEY_IN_HEADER;
import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_SECRET;
import static top.kkuily.xingbackend.constant.user.UserInfo.USER_AVATAR_IN_ALIYUN_OSS_PATH;
import static top.kkuily.xingbackend.constant.user.UserInfo.USER_MAX_AVATAR_SIZE;

/**
 * @author 小K
 */
@RestController
@RequestMapping("user-upload")
public class UserAvatarController {

    @Resource
    private AliyunOSSService aliyunOSSService;

    /**
     * @param avatar  MultipartFile
     * @param request HttpServletRequest
     * @return Result
     * @description 用户上传头像
     */
    @PostMapping("user-avatar")
    @UserAuthToken
    public Result uploadUserAvatar(@RequestBody MultipartFile avatar, HttpServletRequest request) {
        // 获取token
        String header = request.getHeader(USER_TOKEN_KEY_IN_HEADER);
        Claims parseToken;
        try {
            parseToken = Token.parse(header, USER_TOKEN_SECRET);
        } catch (Exception e) {
            return Result.fail(401, "验证错误，上传失败", MsgType.ERROR_MESSAGE);
        }
        // 获取后缀名
        String avatarName = avatar.getOriginalFilename();
        String extension = avatarName.substring(avatarName.lastIndexOf(".") + 1);
        // 获取用户ID
        String userId = (String) parseToken.get("id");
        String isUpload = aliyunOSSService.upload(USER_AVATAR_IN_ALIYUN_OSS_PATH + userId + "_" + System.currentTimeMillis() / 1000 + "." + extension, avatar, USER_MAX_AVATAR_SIZE);
        if (StringUtils.isEmpty(isUpload)) {
            return Result.fail(403, "上传失败", MsgType.ERROR_MESSAGE);
        } else {
            return Result.success("上传成功", isUpload);
        }
    }
}
