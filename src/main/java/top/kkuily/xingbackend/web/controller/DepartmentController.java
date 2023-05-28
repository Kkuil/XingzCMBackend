package top.kkuily.xingbackend.web.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kkuily.xingbackend.model.po.Department;
import top.kkuily.xingbackend.model.po.Role;
import top.kkuily.xingbackend.service.IDepartmentService;
import top.kkuily.xingbackend.service.IRoleService;
import top.kkuily.xingbackend.utils.Result;

import java.util.List;

/**
 * @author 小K
 * @description 角色相关接口
 */
@RestController
public class DepartmentController {

    @Resource
    private IDepartmentService departmentService;

    /**
     * 获取全部角色
     * @return Result
     */
    @GetMapping("dept")
    public Result listRoles() {
        List<Department> list = departmentService.list();
        return Result.success("获取成功", list);
    }
}
