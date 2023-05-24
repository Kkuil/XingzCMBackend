package top.kkuily.xingbackend.web.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kkuily.xingbackend.constant.commons.Api;
import top.kkuily.xingbackend.model.po.Role;
import top.kkuily.xingbackend.service.IRoleService;
import top.kkuily.xingbackend.utils.Result;

import java.util.List;

/**
 * @author 小K
 * @description 角色相关接口
 */
@RestController
@RequestMapping(Api.REQUEST_PREFIX)
public class RoleController {

    @Resource
    private IRoleService roleService;

    /**
     * 获取全部角色
     * @return Result
     */
    @GetMapping("role")
    public Result listRoles() {
        List<Role> list = roleService.list();
        return Result.success("获取成功", list);
    }
}
