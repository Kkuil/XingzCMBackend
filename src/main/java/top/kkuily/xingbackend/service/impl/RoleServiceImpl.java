package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import top.kkuily.xingbackend.model.dto.response.ListResDTO;
import top.kkuily.xingbackend.model.po.Role;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.role.list.RoleListFilterVO;
import top.kkuily.xingbackend.model.vo.role.list.RoleListParamsVO;
import top.kkuily.xingbackend.model.vo.role.list.RoleListSortVO;
import top.kkuily.xingbackend.service.IAuthService;
import top.kkuily.xingbackend.service.IRoleService;
import top.kkuily.xingbackend.mapper.RoleMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.utils.ErrorType;
import top.kkuily.xingbackend.utils.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static top.kkuily.xingbackend.constant.commons.Global.MAX_COUNT_PER_LIST;

/**
 * @author 小K
 * @description 针对表【role】的数据库操作Service实现
 * @createDate 2023-05-21 16:12:27
 */
@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private IAuthService authService;

    /**
     * @param roleListParams ListParamsVO
     * @return Result
     * @author 小K
     * @description 分页查询
     */
    @Override
    public Result getList(ListParamsVO<RoleListParamsVO, RoleListSortVO, RoleListFilterVO> roleListParams) {
        // 1. 获取数据
        RoleListParamsVO params = roleListParams.getParams();
        RoleListSortVO sort = roleListParams.getSort();
        RoleListFilterVO filter = roleListParams.getFilter();
        ListPageVO page = roleListParams.getPage();

        // 2. 将bean转化为map对象
        Map<String, Object> paramsMap = roleListParams.getParams().beanToMapWithLimitField();

        // 3. 查询数据
        QueryWrapper<Role> roleListQuery = new QueryWrapper<>();
        roleListQuery
                .allEq(paramsMap, false)
                .orderBy(true, "ascend".equals(sort.getCreatedTime()), "createdTime")
                .orderBy(true, "ascend".equals(sort.getModifiedTime()), "modifiedTime");
        // 3.1 因为前端的小Bug，传递的数据有问题，在这里提前做判断，增强代码的健壮性
        if (filter.getAuthList() != null) {
            roleListQuery.in(true, "authList", filter.getAuthList());
        }
        // 3.2 因为前端的小Bug，传递的数据有问题，在这里提前做判断，增强代码的健壮性
        if (
                params.getCreatedTime() != null
                        &&
                        !("{".equals(params.getCreatedTime().getStartTime()))
                        &&
                        !("\"".equals(params.getCreatedTime().getEndTime()))
        ) {
            roleListQuery
                    .between(
                            true,
                            "createdTime",
                            params.getCreatedTime().getStartTime(),
                            params.getCreatedTime().getEndTime()
                    );
        }
        if (
                params.getModifiedTime() != null
                        &&
                        !("{".equals(params.getModifiedTime().getStartTime()))
                        &&
                        !("\"".equals(params.getModifiedTime().getEndTime()))
        ) {
            roleListQuery
                    .between(
                            true,
                            "modifiedTime",
                            params.getModifiedTime().getStartTime(),
                            params.getModifiedTime().getEndTime()
                    );
        }

        // 附加：防爬虫
        if (page.getPageSize() >= MAX_COUNT_PER_LIST) {
            return Result.fail(403, "爬虫无所遁形，禁止访问", ErrorType.REDIRECT);
        }

        // 4. 分页查询
        Page<Role> rolePageC = new Page<>(page.getCurrent(), page.getPageSize());
        // 4.1 查询未分页时的数据总数
        List<Role> roleNotPage = roleMapper.selectList(roleListQuery);
        // 4.2 查询分页后的数据
        Page<Role> rolePage = roleMapper.selectPage(rolePageC, roleListQuery);

        log.info("current: {}", page.getCurrent());
        log.info("pageSize: {}", page.getPageSize());
        log.info("total: {}", roleNotPage.size());
        log.info("roles: {}", rolePage);

        // 5. 封装数据
        ListResDTO<Role> roleListRes = new ListResDTO<>();
        roleListRes.setCurrent(page.getCurrent());
        roleListRes.setPageSize(page.getPageSize());
        roleListRes.setList(rolePage.getRecords());
        roleListRes.setTotal(roleNotPage.size());
        return Result.success("获取成功", roleListRes);
    }
}




