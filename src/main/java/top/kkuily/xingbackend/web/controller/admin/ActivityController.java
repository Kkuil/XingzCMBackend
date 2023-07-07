package top.kkuily.xingbackend.web.controller.admin;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.kkuily.xingbackend.anotation.Permission;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.model.dto.request.activity.ActivityAddBodyDTO;
import top.kkuily.xingbackend.model.dto.request.activity.ActivityUpdateBodyDTO;
import top.kkuily.xingbackend.model.enums.AuthEnums;
import top.kkuily.xingbackend.model.enums.IsDeletedEnums;
import top.kkuily.xingbackend.model.po.Activity;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.activity.list.ActivityListFilterVO;
import top.kkuily.xingbackend.model.vo.activity.list.ActivityListParamsVO;
import top.kkuily.xingbackend.model.vo.activity.list.ActivityListSortVO;
import top.kkuily.xingbackend.service.IActivityService;
import top.kkuily.xingbackend.utils.CipherUtils;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.Token;
import top.kkuily.xingbackend.utils.ValidateUtils;

/**
 * @author 小K
 * @description 活动相关接口
 */
@RestController
@Slf4j
public class ActivityController {
    @Resource
    private IActivityService activityService;

    /**
     * @param params ActivityListParamsVO
     * @param sort   ActivityListSortVO
     * @return Result
     * @description 活动分页查询接口
     */
    @GetMapping("activity")
    @Permission(authId = AuthEnums.ACTIVITY_LIST)
    public Result getList(String params, String sort, String filter, String page) {
        log.info("page: {}", params);
        ActivityListParamsVO paramsBean = JSONUtil.toBean(params, ActivityListParamsVO.class);
        ActivityListSortVO sortBean = JSONUtil.toBean(sort, ActivityListSortVO.class);
        ActivityListFilterVO filterBean = JSONUtil.toBean(filter, ActivityListFilterVO.class);
        ListPageVO pageBean = JSONUtil.toBean(page, ListPageVO.class);
        ListParamsVO<ActivityListParamsVO, ActivityListSortVO, ActivityListFilterVO> listParams = new ListParamsVO<>(paramsBean, sortBean, filterBean, pageBean);
//        return activityService.getList(listParams);
        return null;
    }

    // region
    // 增删改查

    /**
     * @param activityAddBodyDTO AuthAddBodyDTO
     * @return Result
     * @description 增
     */
    @PostMapping("activity")
    @Permission(authId = AuthEnums.ACTIVITY_ADD)
    public Result add(@RequestBody ActivityAddBodyDTO activityAddBodyDTO) {
        return null;
    }

    /**
     * @param id String
     * @return Result
     * @description 删
     */
    @DeleteMapping("activity")
    @Permission(authId = AuthEnums.ACTIVITY_DEL)
    public Result del(String id, HttpServletRequest request) {
        return null;
    }

    /**
     * @param activityUpdateBodyDTO AuthUpdateBodyDTO
     * @return Result
     * @description 改
     */
    @PutMapping("activity")
    @Permission(authId = AuthEnums.ACTIVITY_UPDATE)
    public Result update(@RequestBody ActivityUpdateBodyDTO activityUpdateBodyDTO) {
        return null;
    }

    /**
     * @param id String
     * @return Result
     * @description 获取某个活动
     */
    @GetMapping("/activity/:id")
    @Permission(authId = AuthEnums.ACTIVITY_CHECK)
    public Result get(@PathParam("id") String id) {
        return null;
    }

    // endregion
}
