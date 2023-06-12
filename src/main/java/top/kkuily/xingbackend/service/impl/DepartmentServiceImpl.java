package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import top.kkuily.xingbackend.model.dto.response.ListResDTO;
import top.kkuily.xingbackend.model.po.Department;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.dept.list.DeptListFilterVO;
import top.kkuily.xingbackend.model.vo.dept.list.DeptListParamsVO;
import top.kkuily.xingbackend.model.vo.dept.list.DeptListSortVO;
import top.kkuily.xingbackend.service.IDepartmentService;
import top.kkuily.xingbackend.mapper.DepartmentMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.utils.Result;

import java.util.List;
import java.util.Map;

import static top.kkuily.xingbackend.constant.commons.Global.MAX_COUNT_PER_LIST;

/**
 * @author 小K
 * @description 针对表【department】的数据库操作Service实现
 * @createDate 2023-05-21 13:03:51
 */
@Service
@Slf4j
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
        implements IDepartmentService {

    @Resource
    private DepartmentMapper deptMapper;

    @Override
    public Result getList(ListParamsVO<DeptListParamsVO, DeptListSortVO, DeptListFilterVO> deptListParams) {
        // 1. 获取数据
        DeptListParamsVO params = deptListParams.getParams();
        DeptListSortVO sort = deptListParams.getSort();
        DeptListFilterVO filter = deptListParams.getFilter();
        ListPageVO page = deptListParams.getPage();

        // 2. 将bean转化为map对象
        Map<String, Object> paramsMap = deptListParams.getParams().beanToMapWithLimitField();

        // 3. 查询数据
        QueryWrapper<Department> deptListQuery = new QueryWrapper<>();
        deptListQuery
                .allEq(paramsMap, false)
                .orderBy(true, "ascend".equals(sort.getCreatedTime()), "createdTime")
                .orderBy(true, "ascend".equals(sort.getModifiedTime()), "modifiedTime");
        // 3.1 因为前端的小Bug，传递的数据有问题，在这里提前做判断，增强代码的健壮性
        if (
                params.getCreatedTime() != null
                        &&
                        !("{".equals(params.getCreatedTime().getStartTime()))
                        &&
                        !("\"".equals(params.getCreatedTime().getEndTime()))
        ) {
            deptListQuery
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
            deptListQuery
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
        Page<Department> deptPageC = new Page<>(page.getCurrent(), page.getPageSize());
        // 4.1 查询未分页时的数据总数
        List<Department> deptNotPage = deptMapper.selectList(deptListQuery);
        // 4.2 查询分页后的数据
        Page<Department> deptPage = deptMapper.selectPage(deptPageC, deptListQuery);

        log.info("current: {}", page.getCurrent());
        log.info("pageSize: {}", page.getPageSize());
        log.info("total: {}", deptNotPage.size());
        log.info("depts: {}", deptPage.getRecords());

        // 5. 封装数据
        ListResDTO<Department> deptListRes = new ListResDTO<>();
        deptListRes.setCurrent(page.getCurrent());
        deptListRes.setPageSize(page.getPageSize());
        deptListRes.setList(deptPage.getRecords());
        deptListRes.setTotal(deptNotPage.size());
        return Result.success("获取成功", deptListRes);
    }
}




