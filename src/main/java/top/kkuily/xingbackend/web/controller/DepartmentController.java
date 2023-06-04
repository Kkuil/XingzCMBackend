package top.kkuily.xingbackend.web.controller;

import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.kkuily.xingbackend.anotation.Permission;
import top.kkuily.xingbackend.model.dto.request.dept.DeptAddBodyDTO;
import top.kkuily.xingbackend.model.dto.request.dept.DeptUpdateBodyDTO;
import top.kkuily.xingbackend.model.enums.IsDeleted;
import top.kkuily.xingbackend.model.po.Department;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.dept.list.DeptListFilterVO;
import top.kkuily.xingbackend.model.vo.dept.list.DeptListParamsVO;
import top.kkuily.xingbackend.model.vo.dept.list.DeptListSortVO;
import top.kkuily.xingbackend.service.IDepartmentService;
import top.kkuily.xingbackend.utils.ErrorType;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.ValidateUtils;

/**
 * @author 小K
 * @description 部门相关接口
 */
@RestController
@Slf4j
public class DepartmentController {

    @Resource
    private IDepartmentService deptService;

    /**
     * @param params AuthListParamsVO
     * @param sort   AuthListSortVO
     * @return Result
     * @description 部门分页查询接口
     */
    @Permission(authId = 1004)
    @GetMapping("dept")
    public Result getList(String params, String sort, String filter, String page) {
        log.info("page: {}", params);
        DeptListParamsVO paramsBean = JSONUtil.toBean(params, DeptListParamsVO.class);
        DeptListSortVO sortBean = JSONUtil.toBean(sort, DeptListSortVO.class);
//        DeptListFilterVO filterBean = JSONUtil.toBean(filter, DeptListFilterVO.class);
        DeptListFilterVO filterBean = null;
        ListPageVO pageBean = JSONUtil.toBean(page, ListPageVO.class);
        ListParamsVO<DeptListParamsVO, DeptListSortVO, DeptListFilterVO> listParams = new ListParamsVO<>(paramsBean, sortBean, filterBean, pageBean);
        return deptService.getList(listParams);
    }


    // region
    // 增删改查

    /**
     * @param deptAddBodyDTO AuthAddBodyDTO
     * @return Result
     * @description 增
     */
    @Permission(authId = 1102)
    @PostMapping("dept")
    public Result add(@RequestBody DeptAddBodyDTO deptAddBodyDTO) {
        try {
            // 1. 判空
            ValidateUtils.validateNotEmpty("部门名", deptAddBodyDTO.getDeptName());
            ValidateUtils.validateNotEmpty("管理者", deptAddBodyDTO.getManagerId());
            ValidateUtils.validateNotEmpty("城市", deptAddBodyDTO.getLocationId());
            // 2. 判长
            if (deptAddBodyDTO.getDeptName() != null) {
                ValidateUtils.validateLength("城市", deptAddBodyDTO.getDeptName(), 5, 10);
            }
        } catch (Exception e) {
            return Result.fail(403, e.getMessage(), ErrorType.ERROR_MESSAGE);
        }
        Department dept = new Department();
        deptAddBodyDTO.convertTo(dept);
        deptService.save(dept);
        return Result.success("添加成功", true);
    }

    /**
     * @param id String
     * @return Result
     * @description 删
     */
    @Permission(authId = 1103)
    @DeleteMapping("dept")
    public Result del(String id) {
        // 1. 判断账号是否存在
        Department deptInTable = deptService.getById(id);
        if (deptInTable == null) {
            return Result.fail(403, "部门不存在", ErrorType.ERROR_MESSAGE);
        }
        // 2. 判断是否被逻辑删除
        String isDeleted = deptInTable.getIsDeleted();
        if (IsDeleted.YES.getValue().equals(isDeleted)) {
            return Result.fail(400, "删除失败", ErrorType.ERROR_MESSAGE);
        }
        boolean isDel = deptService.removeById(id);
        if (isDel) {
            return Result.success("删除成功", true);
        } else {
            return Result.fail(403, "删除失败", ErrorType.ERROR_MESSAGE);
        }
    }

    /**
     * @param deptUpdateBodyDTO AuthUpdateBodyDTO
     * @return Result
     * @description 改
     */
    @Permission(authId = 1101)
    @PutMapping("dept")
    public Result update(@RequestBody DeptUpdateBodyDTO deptUpdateBodyDTO) {
        log.info("deptUpdateBodyDTO: {}", deptUpdateBodyDTO);
        String id = deptUpdateBodyDTO.getId();
        try {
            ValidateUtils.validateNotEmpty("部门标识符", id);
        } catch (Exception e) {
            return Result.fail(401, "参数异常", ErrorType.ERROR_MESSAGE);
        }
        // 判断账号是否存在
        Department deptInTable = deptService.getById(id);
        if (deptInTable == null) {
            return Result.fail(403, "部门不存在", ErrorType.ERROR_MESSAGE);
        }
        Department dept = new Department();
        deptUpdateBodyDTO.convertTo(dept);
        boolean isDel = deptService.updateById(dept);
        if (isDel) {
            return Result.success("更新成功", true);
        } else {
            return Result.fail(403, "删除失败", ErrorType.ERROR_MESSAGE);
        }
    }

    /**
     * @param id String
     * @return Result
     * @description 获取某个部门
     */
    @Permission(authId = 1100)
    @GetMapping("/dept/:id")
    public Result get(@PathParam("id") String id) {
        // 1. 判断账号是否存在
        Department deptInTable = deptService.getById(id);
        if (deptInTable == null) {
            return Result.fail(403, "部门不存在", ErrorType.ERROR_MESSAGE);
        }
        // 2. 判断是否被逻辑删除
        String isDeleted = deptInTable.getIsDeleted();
        if (IsDeleted.NO.getValue().equals(isDeleted)) {
            return Result.success("获取成功", true);
        } else {
            return Result.success("获取成功，该部门已被删除", true);
        }
    }

    // endregion
}
