package top.kkuily.xingbackend.web.controller.admin;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.model.dto.request.user.UserAddBodyDTO;
import top.kkuily.xingbackend.model.dto.request.user.UserUpdateBodyDTO;
import top.kkuily.xingbackend.model.po.User;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.user.UserByUsernameForChat;
import top.kkuily.xingbackend.model.vo.user.list.UserListFilterVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListParamsVO;
import top.kkuily.xingbackend.model.vo.user.list.UserListSortVO;
import top.kkuily.xingbackend.service.IUserService;
import top.kkuily.xingbackend.utils.*;

import java.util.ArrayList;
import java.util.List;

import static top.kkuily.xingbackend.constant.commons.Auth.USER_MAIN_ID;
import static top.kkuily.xingbackend.constant.user.UserInfo.USER_DEFAULT_NAME_PREFIX;
import static top.kkuily.xingbackend.constant.user.Auth.USER_DEFAULT_PASSWORD;

/**
 * @author 小K
 * @description 用户相关接口
 */
@RestController
@Slf4j
public class UserController {
    @Resource
    private IUserService userService;

    /**
     * 用户分页查询接口
     *
     * @param params Object
     * @param sort   Object
     * @param filter Object
     * @return Result
     */
    @GetMapping("user")
    public Result getList(String params, String sort, String filter, String page) {
        UserListParamsVO paramsBean = JSONUtil.toBean(params, UserListParamsVO.class);
        UserListSortVO sortBean = JSONUtil.toBean(sort, UserListSortVO.class);
        UserListFilterVO filterBean = JSONUtil.toBean(filter, UserListFilterVO.class);
        ListPageVO pageBean = JSONUtil.toBean(page, ListPageVO.class);
        ListParamsVO<UserListParamsVO, UserListSortVO, UserListFilterVO> userListParams = new ListParamsVO<>(paramsBean, sortBean, filterBean, pageBean);
        return userService.getList(userListParams);
    }

    // region
    // 增删改查

    /**
     * @param userAddBodyDTO UserAddBodyDTO
     * @return Result
     * @description 增
     */
    @PostMapping("user")
    @Transactional(rollbackFor = Exception.class)
    public Result add(@RequestBody UserAddBodyDTO userAddBodyDTO) {
        try {
            if (!StringUtils.isEmpty(userAddBodyDTO.getUsername())) {
                ValidateUtils.validateLength("用户名", userAddBodyDTO.getUsername(), 8, 17);
            }
            ValidateUtils.validateNotEmpty("电话号", userAddBodyDTO.getPhone());
            ValidateUtils.validateLength("电话号", userAddBodyDTO.getPhone(), 11, 11);
            ValidateUtils.validateMobile("电话号", userAddBodyDTO.getPhone());
            if (!StringUtils.isEmpty(userAddBodyDTO.getEmail())) {
                ValidateUtils.validateMail("邮箱", userAddBodyDTO.getEmail());
            }
        } catch (Exception e) {
            return Result.fail(403, e.getMessage(), MsgType.ERROR_MESSAGE);
        }

        // 判断用户名和手机号是否已被占用
        String username = userAddBodyDTO.getUsername();
        String phone = userAddBodyDTO.getPhone();
        if (!StringUtils.isEmpty(username) || !StringUtils.isEmpty(phone)) {
            QueryWrapper<User> userWrapper = new QueryWrapper<>();
            userWrapper
                    .eq("username", username)
                    .or()
                    .eq("phone", phone);
            User userInTable = userService.getOne(userWrapper);
            if (userInTable != null) {
                return Result.fail(401, "该用户名或手机号已被占用，请重新输入", MsgType.ERROR_MESSAGE);
            }
        }
        // 随机uuid当后缀
        String suffix = StringUtils.split(UUID.randomUUID(true).toString(), "-")[0];
        User user = new User();
        userAddBodyDTO.convertTo(user);
        if (StringUtils.isEmpty(userAddBodyDTO.getUsername())) {
            // 用户名为空，设置默认用户名
            user.setUsername(USER_DEFAULT_NAME_PREFIX + suffix);
        }
        if (StringUtils.isEmpty(userAddBodyDTO.getPassword())) {
            // 密码为空，设置默认密码
            user.setPassword(CipherUtils.md5(USER_DEFAULT_PASSWORD));
        }
        userService.save(user);
        return Result.success("添加成功", true);
    }

    /**
     * @param id String
     * @return Result
     * @description 删
     */
    @DeleteMapping("user")
    public Result del(String id) {
        // 1. 判断账号是否存在
        User userInTable = userService.getById(id);
        if (userInTable == null) {
            return Result.fail(403, "账号不存在", MsgType.ERROR_MESSAGE);
        }
        // 2. 判断是否删除的是主账号
        if (USER_MAIN_ID.equals(id)) {
            return Result.fail(401, "主账号不允许被删除", MsgType.ERROR_MESSAGE);
        }
        // 判断是否被删除
        boolean isDel = userService.removeById(id);
        if (isDel) {
            return Result.success("删除成功", true);
        } else {
            return Result.fail(403, "删除失败", MsgType.ERROR_MESSAGE);
        }
    }

    /**
     * @param userUpdateBodyDTO UserUpdateBodyDTO
     * @return Result
     * @description 改
     */
    @PutMapping("user")
    public Result update(@RequestBody UserUpdateBodyDTO userUpdateBodyDTO) {
        try {
            if (!StringUtils.isEmpty(userUpdateBodyDTO.getPhone())) {
                ValidateUtils.validateLength("手机号", userUpdateBodyDTO.getPhone(), 11, 11);
                ValidateUtils.validateMobile("手机号", userUpdateBodyDTO.getPhone());
            }
            if (!StringUtils.isEmpty(userUpdateBodyDTO.getUsername())) {
                ValidateUtils.validateLength("用户名", userUpdateBodyDTO.getUsername(), 8, 17);
            }
            if (!StringUtils.isEmpty(userUpdateBodyDTO.getEmail())) {
                ValidateUtils.validateMail("邮箱", userUpdateBodyDTO.getEmail());
            }
            // 判断账号是否存在
            User userInTable = userService.getById(userUpdateBodyDTO.getId());
            if (userInTable == null) {
                return Result.fail(403, "账号不存在", MsgType.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            return Result.fail(403, e.getMessage(), MsgType.ERROR_MESSAGE);
        }
        User user = new User();
        userUpdateBodyDTO.convertTo(user);
        boolean isUpdate = userService.updateById(user);
        if (isUpdate) {
            return Result.success("更新成功", true);
        } else {
            return Result.fail(403, "更新失败", MsgType.ERROR_MESSAGE);
        }
    }

    /**
     * @param username String
     * @return Result
     * @description 通过用户名获取用户
     */
    @GetMapping("/user/{username}")
    public Result getByUsername(@PathVariable String username) {
        // 判断账号是否存在
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.like("username", username);
        List<User> listUser = userService.list(userWrapper);
        if (listUser.size() == 0) {
            return Result.fail(403, "用户不存在", MsgType.ERROR_MESSAGE);
        }
        List<UserByUsernameForChat> listChatInfo = new ArrayList<>(listUser.size());
        UserByUsernameForChat.userListConvertToChatList(listUser, listChatInfo);
        return Result.success("查询成功", listChatInfo);
    }
    // endregion
}
