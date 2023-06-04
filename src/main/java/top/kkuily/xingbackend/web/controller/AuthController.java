package top.kkuily.xingbackend.web.controller;

import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.kkuily.xingbackend.anotation.Permission;
import top.kkuily.xingbackend.model.dto.request.auth.AuthAddBodyDTO;
import top.kkuily.xingbackend.model.dto.request.auth.AuthUpdateBodyDTO;
import top.kkuily.xingbackend.model.enums.IsDeleted;
import top.kkuily.xingbackend.model.po.Auth;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.auth.list.AuthListFilterVO;
import top.kkuily.xingbackend.model.vo.auth.list.AuthListParamsVO;
import top.kkuily.xingbackend.model.vo.auth.list.AuthListSortVO;
import top.kkuily.xingbackend.service.IAuthService;
import top.kkuily.xingbackend.utils.ErrorType;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.ValidateUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author 小K
 * @description 权限相关接口
 */
@RestController
@Slf4j
public class AuthController {

    @Resource
    private IAuthService authService;

    /**
     * @param params AuthListParamsVO
     * @param sort   AuthListSortVO
     * @return Result
     * @description 权限分页查询接口
     */
    @Permission(authId = 1004)
    @GetMapping("auth")
    public Result getList(String params, String sort, String filter, String page) {
        log.info("page: {}", params);
        AuthListParamsVO paramsBean = JSONUtil.toBean(params, AuthListParamsVO.class);
        AuthListSortVO sortBean = JSONUtil.toBean(sort, AuthListSortVO.class);
        // AuthListFilterVO filterBean = JSONUtil.toBean(filter, AuthListFilterVO.class);
        AuthListFilterVO filterBean = null;
        ListPageVO pageBean = JSONUtil.toBean(page, ListPageVO.class);
        ListParamsVO<AuthListParamsVO, AuthListSortVO, AuthListFilterVO> listParams = new ListParamsVO<>(paramsBean, sortBean, filterBean, pageBean);
        return authService.getList(listParams);
    }

    // region
    // 增删改查

    /**
     * @param authAddBodyDTO AuthAddBodyDTO
     * @return Result
     * @description 增
     */
    @Permission(authId = 1102)
    @PostMapping("auth")
    public Result add(@RequestBody AuthAddBodyDTO authAddBodyDTO) {
        try {
            // 1. 判空
            ValidateUtils.validateNotEmpty("权限名", authAddBodyDTO.getAuthName());
            ValidateUtils.validateNotEmpty("权限路由", authAddBodyDTO.getAuthRoute());
            ValidateUtils.validateNotEmpty("描述", authAddBodyDTO.getDescription());
            // 2. 判长
            if (authAddBodyDTO.getDescription() != null) {
                ValidateUtils.validateLength("描述", authAddBodyDTO.getDescription(), 5, 20);
            }
        } catch (Exception e) {
            return Result.fail(403, e.getMessage(), ErrorType.ERROR_MESSAGE);
        }
        Auth auth = new Auth();
        authAddBodyDTO.convertTo(auth);
        authService.save(auth);
        return Result.success("添加成功", true);
    }

    /**
     * @param id String
     * @return Result
     * @description 删
     */
    @Permission(authId = 1103)
    @DeleteMapping("auth")
    public Result del(String id) {
        // 1. 判断账号是否存在
        Auth authInTable = authService.getById(id);
        if (authInTable == null) {
            return Result.fail(403, "权限不存在", ErrorType.ERROR_MESSAGE);
        }
        // 2. 判断是否被逻辑删除
        String isDeleted = authInTable.getIsDeleted();
        if (IsDeleted.YES.getValue().equals(isDeleted)) {
            return Result.fail(400, "删除失败", ErrorType.ERROR_MESSAGE);
        }
        boolean isDel = authService.removeById(id);
        if (isDel) {
            return Result.success("删除成功", true);
        } else {
            return Result.fail(403, "删除失败", ErrorType.ERROR_MESSAGE);
        }
    }

    /**
     * @param authUpdateBodyDTO AuthUpdateBodyDTO
     * @return Result
     * @description 改
     */
    @Permission(authId = 1101)
    @PutMapping("auth")
    public Result update(@RequestBody AuthUpdateBodyDTO authUpdateBodyDTO) {
        log.info("authUpdateBodyDTO: {}", authUpdateBodyDTO);
        String id = authUpdateBodyDTO.getId();
        try {
            ValidateUtils.validateNotEmpty("权限标识符", id);
        } catch (Exception e) {
            return Result.fail(401, "参数异常", ErrorType.ERROR_MESSAGE);
        }
        // 判断账号是否存在
        Auth authInTable = authService.getById(id);
        if (authInTable == null) {
            return Result.fail(403, "权限不存在", ErrorType.ERROR_MESSAGE);
        }
        Auth auth = new Auth();
        authUpdateBodyDTO.convertTo(auth);
        boolean isDel = authService.updateById(auth);
        if (isDel) {
            return Result.success("更新成功", true);
        } else {
            return Result.fail(403, "删除失败", ErrorType.ERROR_MESSAGE);
        }
    }

    /**
     * @param id String
     * @return Result
     * @description 获取某个权限
     */
    @Permission(authId = 1100)
    @GetMapping("/auth/:id")
    public Result get(@PathParam("id") String id) {
        // 1. 判断账号是否存在
        Auth authInTable = authService.getById(id);
        if (authInTable == null) {
            return Result.fail(403, "权限不存在", ErrorType.ERROR_MESSAGE);
        }
        // 2. 判断是否被逻辑删除
        String isDeleted = authInTable.getIsDeleted();
        if (IsDeleted.NO.getValue().equals(isDeleted)) {
            return Result.success("获取成功", true);
        } else {
            return Result.success("获取成功，该权限已被删除", true);
        }
    }

    // endregion

    /**
     * @param ids String
     * @return Result
     * @description 通过id集合进行批量查询权限信息
     */
    @GetMapping("/auth/batchIds")
    @Permission(authId = 1007)
    private Result getAuthWithIds(String ids) {
        String[] authIds;
        try {
            authIds = StringUtils.split(ids, ",");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<Auth> auths = authService.listByIds(Arrays.asList(authIds));
        return Result.success("获取成功", auths);
    }
}
