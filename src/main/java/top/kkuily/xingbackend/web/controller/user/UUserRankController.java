package top.kkuily.xingbackend.web.controller.user;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kkuily.xingbackend.anotation.ApiSignAuth;
import top.kkuily.xingbackend.service.IUserRankService;
import top.kkuily.xingbackend.utils.Result;

/**
 * @author 小K
 * @description 文章分类相关接口
 */
@RestController
@Slf4j
public class UUserRankController {

    @Resource
    private IUserRankService userRankService;

    /**
     * @param current  int
     * @param pageSize int
     * @return Result
     * @description 获取用户等级列表
     */
    @GetMapping("user-rank")
    @ApiSignAuth
    public Result listUserRank(int current, int pageSize, int sort) {
        return userRankService.listUserRank(current, pageSize, sort);
    }
}
