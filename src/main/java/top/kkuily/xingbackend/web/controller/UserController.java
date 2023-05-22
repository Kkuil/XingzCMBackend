package top.kkuily.xingbackend.web.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kkuily.xingbackend.constant.API;
import top.kkuily.xingbackend.model.po.User;
import top.kkuily.xingbackend.service.IUserService;
import top.kkuily.xingbackend.utils.Result;

import java.util.List;

/**
 * @author 小K
 */
@RestController
@RequestMapping(API.REQUEST_PREFIX)
@Slf4j
public class UserController {
    @Resource
    private IUserService userService;

    @PostMapping("/user")
    public Result addUser() {
        List<User> list = userService.list();
        log.info("用户：{}", list);
        return Result.success("成功", list);
    }
}
