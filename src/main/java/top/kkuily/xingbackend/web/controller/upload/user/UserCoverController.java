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
import top.kkuily.xingbackend.service.other.AliyunOSSService;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.Token;

import static top.kkuily.xingbackend.constant.aritcle.ArticleInfo.ARTICLE_COVER_IN_ALIYUN_OSS_PATH;
import static top.kkuily.xingbackend.constant.aritcle.ArticleInfo.MAX_COVER_SIZE;
import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_KEY_IN_HEADER;
import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_SECRET;

/**
 * @author 小K
 */
@RestController
@RequestMapping("user-upload")
public class UserCoverController {

    @Resource
    private AliyunOSSService aliyunOSSService;

    @PostMapping("uarticle-upload")
    @UserAuthToken
    public Result upload(@RequestBody MultipartFile cover, HttpServletRequest request) {
        // 获取token
        String header = request.getHeader(USER_TOKEN_KEY_IN_HEADER);
        Claims parseToken;
        try {
            parseToken = Token.parse(header, USER_TOKEN_SECRET);
        } catch (Exception e) {
            return Result.fail(401, "验证错误，上传失败", MsgType.ERROR_MESSAGE);
        }
        // 获取后缀名
        String fileName = cover.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 获取用户ID
        String userId = (String) parseToken.get("id");
        String isUpload = aliyunOSSService.upload(ARTICLE_COVER_IN_ALIYUN_OSS_PATH + userId + "_" + System.currentTimeMillis() / 1000 + "." + fileExtension, cover, MAX_COVER_SIZE);
        if (StringUtils.isEmpty(isUpload)) {
            return Result.fail(403, "上传失败", MsgType.ERROR_MESSAGE);
        } else {
            return Result.success("上传成功", isUpload);
        }
    }
}
