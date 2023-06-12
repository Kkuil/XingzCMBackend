package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import top.kkuily.xingbackend.model.dto.response.ListResDTO;
import top.kkuily.xingbackend.model.po.Auth;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.auth.list.AuthListFilterVO;
import top.kkuily.xingbackend.model.vo.auth.list.AuthListParamsVO;
import top.kkuily.xingbackend.model.vo.auth.list.AuthListSortVO;
import top.kkuily.xingbackend.service.IAuthService;
import top.kkuily.xingbackend.mapper.AuthMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.utils.Result;

import java.util.List;
import java.util.Map;

import static top.kkuily.xingbackend.constant.commons.Global.MAX_COUNT_PER_LIST;

/**
* @author 小K
* @description 针对表【auth】的数据库操作Service实现
* @createDate 2023-05-31 11:00:28
*/
@Service
@Slf4j
public class AuthServiceImpl extends ServiceImpl<AuthMapper, Auth>
    implements IAuthService {

    @Resource
    private AuthMapper authMapper;

    @Override
    public Result getList(ListParamsVO<AuthListParamsVO, AuthListSortVO, AuthListFilterVO> authListParams) {
        // 1. 获取数据
        AuthListParamsVO params = authListParams.getParams();
        AuthListSortVO sort = authListParams.getSort();
        ListPageVO page = authListParams.getPage();

        // 2. 将bean转化为map对象
        Map<String, Object> paramsMap = authListParams.getParams().beanToMapWithLimitField();

        // 3. 查询数据
        QueryWrapper<Auth> authListQuery = new QueryWrapper<>();
        authListQuery
                .allEq(paramsMap, false)
                .orderBy(true, "ascend".equals(sort.getCreatedTime()), "createdTime")
                .orderBy(true, "ascend".equals(sort.getModifiedTime()), "modifiedTime");
        // 3.2 因为前端的小Bug，传递的数据有问题，在这里提前做判断，增强代码的健壮性
        if (
                params.getCreatedTime() != null
                        &&
                        !("{".equals(params.getCreatedTime().getStartTime()))
                        &&
                        !("\"".equals(params.getCreatedTime().getEndTime()))
        ) {
            authListQuery
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
            authListQuery
                    .between(
                            true,
                            "modifiedTime",
                            params.getModifiedTime().getStartTime(),
                            params.getModifiedTime().getEndTime()
                    );
        }

        // 附加：防爬虫
        if (page.getPageSize() >= MAX_COUNT_PER_LIST) {
            return Result.fail(403, "爬虫无所遁形，禁止访问", MsgType.REDIRECT);
        }

        // 4. 分页查询
        Page<Auth> authPageC = new Page<>(page.getCurrent(), page.getPageSize());
        // 4.1 查询未分页时的数据总数
        List<Auth> authNotPage = authMapper.selectList(authListQuery);
        // 4.2 查询分页后的数据
        Page<Auth> authPage = authMapper.selectPage(authPageC, authListQuery);

        log.info("current: {}", page.getCurrent());
        log.info("pageSize: {}", page.getPageSize());
        log.info("total: {}", authNotPage.size());
        log.info("auths: {}", authPage.getRecords());

        // 5. 封装数据
        ListResDTO<Auth> authListRes = new ListResDTO<>();
        authListRes.setCurrent(page.getCurrent());
        authListRes.setPageSize(page.getPageSize());
        authListRes.setList(authPage.getRecords());
        authListRes.setTotal(authNotPage.size());
        return Result.success("获取成功", authListRes);
    }
}




