package top.kkuily.xingbackend.web.controller;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.kkuily.xingbackend.anotation.Permission;
import top.kkuily.xingbackend.model.po.Admin;
import top.kkuily.xingbackend.model.po.User;
import top.kkuily.xingbackend.service.IAdminService;
import top.kkuily.xingbackend.service.IUserService;
import top.kkuily.xingbackend.utils.AliyunOss;
import top.kkuily.xingbackend.utils.ErrorType;
import top.kkuily.xingbackend.utils.Result;

import java.io.IOException;
import java.util.Objects;

/**
 * @author 小K
 * @description 上传文件接口
 */
@RestController
@RequestMapping("/upload")
@ConfigurationProperties("spring.servlet.multipart")
public class Upload {

    @Resource
    private IUserService userService;

    @Resource
    private IAdminService adminService;

    /**
     * 阿里云oss管理员头像存储路径
     */
    private static final String ALIYUN_OSS_ADMIN_AVATAR_PATH = "resources/admin-avatars/";

    /**
     * 阿里云oss用户头像存储路径
     */
    private static final String ALIYUB_OSS_USER_AVATAR_PATH = "resources/user-avatars/";

    private String maxFileSize;

    /**
     * 图片类型常量
     */
    private static final String IMAGE_TYPE = "image";

    @Permission(authId = 10017)
    @PostMapping("/avatar")
    public Result avatar(String type, MultipartFile avatar) {
        // 1. 判空
        if (avatar.isEmpty()) {
            return Result.fail(401, "至少上传一张图片", ErrorType.NOTIFICATION);
        }
        // 2. 判断文件类型
        String contentType = Objects.requireNonNull(StringUtils.split(avatar.getContentType(), "/"))[0];
        if (!IMAGE_TYPE.equals(contentType)) {
            return Result.fail(401, "只允许上传图片格式", ErrorType.ERROR_MESSAGE);
        }
        // 3. 判断文件大小
        double size = avatar.getSize() / (1024.0 * 1024.0);
        if (size > Integer.parseInt(StringUtils.split(maxFileSize, "MB")[0])) {
            return Result.fail(401, "图片大小不能超过" + maxFileSize, ErrorType.ERROR_MESSAGE);
        }
        try {
            // 4. 获取文件名
            String filename = avatar.getOriginalFilename();
            String id = Objects.requireNonNull(StringUtils.split(filename, "-"))[1];
            if (id == null) {
                return Result.fail(401, "格式不正确，请重新上传", ErrorType.ERROR_MESSAGE);
            }
            // 5. 判断是否合法
            if ("admin".equals(type)) {
                Admin admin = adminService.getById(id);
                if (admin == null) {
                    return Result.fail(401, "Access denied", ErrorType.ERROR_MESSAGE);
                }
                // 6. 上传图片到aliyunoss
                String uri = AliyunOss.upload(ALIYUN_OSS_ADMIN_AVATAR_PATH + filename, avatar.getResource().getFile());
                if (uri.isEmpty()) {
                    return Result.fail(405, "上传失败", ErrorType.ERROR_MESSAGE);
                }
                admin.setId(id);
                admin.setAvatar(uri);
                boolean isSave = adminService.save(admin);
                if (!isSave) {
                    return Result.fail(400, "上传失败", ErrorType.ERROR_MESSAGE);
                } else {
                    return Result.success("上传成功", uri);
                }
            } else {
                User user = userService.getById(id);
                if (user == null) {
                    return Result.fail(401, "Access denied", ErrorType.ERROR_MESSAGE);
                }
                // 6. 上传图片到aliyunoss
                String uri = AliyunOss.upload(ALIYUB_OSS_USER_AVATAR_PATH + filename, avatar.getResource().getFile());
                if (uri.isEmpty()) {
                    return Result.fail(405, "上传失败", ErrorType.ERROR_MESSAGE);
                }
                user.setId(id);
                user.setAvatar(uri);
                boolean isSave = userService.save(user);
                if (!isSave) {
                    return Result.fail(400, "上传失败", ErrorType.ERROR_MESSAGE);
                } else {
                    return Result.success("上传成功", uri);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail(403, "上传失败", ErrorType.ERROR_MESSAGE);
        }
    }
}
