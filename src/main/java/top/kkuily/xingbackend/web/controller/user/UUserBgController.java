package top.kkuily.xingbackend.web.controller.user;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kkuily.xingbackend.anotation.ApiSignAuth;
import top.kkuily.xingbackend.anotation.SelfVerification;
import top.kkuily.xingbackend.anotation.UserAuthToken;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.model.po.User;
import top.kkuily.xingbackend.model.po.UserBg;
import top.kkuily.xingbackend.service.IUserBgService;
import top.kkuily.xingbackend.service.IUserService;
import top.kkuily.xingbackend.utils.Result;

import java.util.List;

/**
 * @author 小K
 * @description 文章分类相关接口
 */
@RestController
@Slf4j
public class UUserBgController {

    @Resource
    private IUserService userService;

    @Resource
    private IUserBgService userBgService;

    /**
     * @return Result
     * @description 获取用户背景图
     */
    @GetMapping("/user-bg")
    public Result list() {
        List<UserBg> list = userBgService.list();
        return Result.success("获取成功", list, MsgType.SILENT);
    }

    /**
     * @return Result
     * @description 更新用户背景图
     */
    @PutMapping("/user-bg")
    @SelfVerification
    @ApiSignAuth
    public Result update(String id, String url) {
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper
                .eq("id", id)
                .set("bgCover", url);
        boolean update = userService.update(userUpdateWrapper);
        if (update) {
            return Result.success("更新成功", true, MsgType.NOTIFICATION);
        } else {
            return Result.fail(403, "更新失败", MsgType.NOTIFICATION);
        }
    }
}
