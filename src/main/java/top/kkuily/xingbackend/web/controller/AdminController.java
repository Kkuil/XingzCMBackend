package top.kkuily.xingbackend.web.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.kkuily.xingbackend.constant.API;
import top.kkuily.xingbackend.model.dto.request.ListParams;
import top.kkuily.xingbackend.model.dto.request.AdminLoginBody;
import top.kkuily.xingbackend.service.IAdminService;
import top.kkuily.xingbackend.utils.Result;

/**
 * @author 小K
 * @description 登录相关接口
 */
@RestController
@RequestMapping(API.REQUEST_PREFIX)
@Slf4j
public class AdminController {
    @Resource
    private IAdminService adminService;

    /**
     * 管理员登录接口
     *
     * @param response       HttpServletResponse
     * @param adminLoginBody AdminLoginBody
     * @return Result
     */
    @PostMapping("admin-login")
    public Result login(HttpServletResponse response, @RequestBody AdminLoginBody adminLoginBody) {
        return adminService.login(response, adminLoginBody);
    }

    /**
     * 权限校验端口
     *
     * @param request HttpServletRequest
     * @return Result
     */
    @PostMapping("admin-auth")
    public Result auth(HttpServletRequest request) {
        return adminService.auth(request);
    }

    /**
     * 分页查询接口
     *
     * @param params Object
     * @param sort Object
     * @param filter Object
     * @return Result
     */
    @GetMapping("admin")
    public Result getList(@RequestParam Object params, @RequestParam Object sort, @RequestParam Object filter) {
        ListParams adminListParams = new ListParams(params, sort, filter);
        return adminService.getList(adminListParams);
    }
}
