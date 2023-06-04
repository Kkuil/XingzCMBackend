package top.kkuily.xingbackend.web.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.kkuily.xingbackend.anotation.Permission;
import top.kkuily.xingbackend.constant.commons.Api;
import top.kkuily.xingbackend.model.dto.request.role.RoleAddBodyDTO;
import top.kkuily.xingbackend.model.dto.request.role.RoleUpdateBodyDTO;
import top.kkuily.xingbackend.model.enums.IsDeleted;
import top.kkuily.xingbackend.model.po.Role;
import top.kkuily.xingbackend.model.po.Role;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.role.list.RoleListFilterVO;
import top.kkuily.xingbackend.model.vo.role.list.RoleListParamsVO;
import top.kkuily.xingbackend.model.vo.role.list.RoleListSortVO;
import top.kkuily.xingbackend.service.IRoleService;
import top.kkuily.xingbackend.utils.CipherUtils;
import top.kkuily.xingbackend.utils.ErrorType;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.ValidateUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author 小K
 * @description 角色相关接口
 */
@RestController
@Slf4j
public class RoleController {

    @Resource
    private IRoleService roleService;

    /**
     * @param params AuthListParamsVO
     * @param sort   AuthListSortVO
     * @return Result
     * @description 角色分页查询接口
     */
    @Permission(authId = 1004)
    @GetMapping("role")
    public Result getList(String params, String sort, String filter, String page) {
        log.info("page: {}", params);
        RoleListParamsVO paramsBean = JSONUtil.toBean(params, RoleListParamsVO.class);
        RoleListSortVO sortBean = JSONUtil.toBean(sort, RoleListSortVO.class);
        RoleListFilterVO filterBean = JSONUtil.toBean(filter, RoleListFilterVO.class);
        ListPageVO pageBean = JSONUtil.toBean(page, ListPageVO.class);
        ListParamsVO<RoleListParamsVO, RoleListSortVO, RoleListFilterVO> listParams = new ListParamsVO<>(paramsBean, sortBean, filterBean, pageBean);
        return roleService.getList(listParams);
    }


    // region
    // 增删改查

    /**
     * @param roleAddBodyDTO AuthAddBodyDTO
     * @return Result
     * @description 增
     */
    @Permission(authId = 1102)
    @PostMapping("role")
    public Result add(@RequestBody RoleAddBodyDTO roleAddBodyDTO) {
        try {
            // 1. 判空
            ValidateUtils.validateNotEmpty("角色名", roleAddBodyDTO.getRoleName());
            ValidateUtils.validateNotEmpty("权限列表", roleAddBodyDTO.getAuthList());
            ValidateUtils.validateNotEmpty("描述", roleAddBodyDTO.getDescription());
            // 2. 判长
            if (roleAddBodyDTO.getDescription() != null) {
                ValidateUtils.validateLength("描述", roleAddBodyDTO.getDescription(), 0, 10);
            }
            // 判断角色名是否存在
            QueryWrapper<Role> roleWrapper = new QueryWrapper<>();
            roleWrapper.eq("roleName", roleAddBodyDTO.getRoleName());
            Role roleInTable = roleService.getOne(roleWrapper);
            if (roleInTable != null) {
                throw new IllegalArgumentException("角色名已存在，请重新输入");
            }
        } catch (Exception e) {
            return Result.fail(403, e.getMessage(), ErrorType.ERROR_MESSAGE);
        }
        Role role = new Role();
        roleAddBodyDTO.convertTo(role);
        // 转换一下存储格式，优化数据库存储大小
        role.setAuthList(StringUtils
                .join(roleAddBodyDTO.getAuthList(), ",")
                .replace("[", "")
                .replace("]", ""));
        // 保存数据
        boolean isSave = roleService.save(role);
        if (isSave) {
            return Result.success("添加成功", true);
        } else {
            return Result.fail(500, "添加失败", ErrorType.ERROR_MESSAGE);
        }
    }

    /**
     * @param id String
     * @return Result
     * @description 删
     */
    @Permission(authId = 1103)
    @DeleteMapping("role")
    public Result del(String id) {
        // 1. 判断账号是否存在
        Role roleInTable = roleService.getById(id);
        if (roleInTable == null) {
            return Result.fail(403, "角色不存在", ErrorType.ERROR_MESSAGE);
        }
        // 2. 判断是否是基础角色
        String[] baseRoleIds = {"1", "2"};
        try {
            if (ArrayUtils.contains(baseRoleIds, id)) {
                throw new IllegalArgumentException("不允许删除基础角色");
            }
        } catch (Exception e) {
            return Result.fail(403, "不允许删除基础角色", ErrorType.ERROR_MESSAGE);
        }
        boolean isDel = roleService.removeById(id);
        if (isDel) {
            return Result.success("删除成功", true);
        } else {
            return Result.fail(403, "删除失败", ErrorType.ERROR_MESSAGE);
        }
    }

    /**
     * @param roleUpdateBodyDTO AuthUpdateBodyDTO
     * @return Result
     * @description 改
     */
    @Permission(authId = 1101)
    @PutMapping("role")
    public Result update(@RequestBody RoleUpdateBodyDTO roleUpdateBodyDTO) {
        log.info("roleUpdateBodyDTO: {}", roleUpdateBodyDTO);
        String id = roleUpdateBodyDTO.getId();
        try {
            ValidateUtils.validateNotEmpty("角色标识符", id);
            ValidateUtils.validateNotEmpty("权限列表", roleUpdateBodyDTO.getAuthList());
        } catch (Exception e) {
            return Result.fail(403, e.getMessage(), ErrorType.ERROR_MESSAGE);
        }
        // 判断账号是否存在
        Role roleInTable = roleService.getById(id);
        if (roleInTable == null) {
            return Result.fail(403, "角色不存在", ErrorType.ERROR_MESSAGE);
        }
        Role role = new Role();
        roleUpdateBodyDTO.convertTo(role);
        boolean isDel = roleService.updateById(role);
        if (isDel) {
            return Result.success("更新成功", true);
        } else {
            return Result.fail(403, "删除失败", ErrorType.ERROR_MESSAGE);
        }
    }

    /**
     * @param id String
     * @return Result
     * @description 获取某个角色
     */
    @Permission(authId = 1100)
    @GetMapping("/role/:id")
    public Result get(@PathParam("id") String id) {
        // 1. 判断账号是否存在
        Role roleInTable = roleService.getById(id);
        if (roleInTable == null) {
            return Result.fail(403, "角色不存在", ErrorType.ERROR_MESSAGE);
        }
        // 2. 判断是否被逻辑删除
        String isDeleted = roleInTable.getIsDeleted();
        if (IsDeleted.NO.getValue().equals(isDeleted)) {
            return Result.success("获取成功", true);
        } else {
            return Result.success("获取成功，该角色已被删除", true);
        }
    }

    // endregion
}
