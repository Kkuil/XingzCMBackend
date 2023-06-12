package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import top.kkuily.xingbackend.model.dto.response.ListResDTO;
import top.kkuily.xingbackend.model.po.Tag;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.tag.list.TagListFilterVO;
import top.kkuily.xingbackend.model.vo.tag.list.TagListParamsVO;
import top.kkuily.xingbackend.model.vo.tag.list.TagListSortVO;
import top.kkuily.xingbackend.service.ITagService;
import top.kkuily.xingbackend.mapper.TagMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.utils.Result;

import java.util.List;
import java.util.Map;

import static top.kkuily.xingbackend.constant.commons.Global.MAX_COUNT_PER_LIST;

/**
 * @author 小K
 * @description 针对表【tag】的数据库操作Service实现
 * @createDate 2023-05-18 11:21:27
 */
@Service
@Slf4j
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
        implements ITagService {
    @Resource
    private TagMapper tagMapper;

    @Override
    public Result getList(ListParamsVO<TagListParamsVO, TagListSortVO, TagListFilterVO> tagListParams) {
        // 1. 获取数据
        TagListParamsVO params = tagListParams.getParams();
        TagListSortVO sort = tagListParams.getSort();
        TagListFilterVO filter = tagListParams.getFilter();
        ListPageVO page = tagListParams.getPage();

        // 2. 将bean转化为map对象
        Map<String, Object> paramsMap = tagListParams.getParams().beanToMapWithLimitField();

        // 3. 查询数据
        QueryWrapper<Tag> tagListQuery = new QueryWrapper<>();
        tagListQuery
                .allEq(paramsMap, false)
                .orderBy(true, "ascend".equals(sort.getCreatedTime()), "createdTime")
                .orderBy(true, "ascend".equals(sort.getModifiedTime()), "modifiedTime");
        // 3.1 因为前端的小Bug，传递的数据有问题，在这里提前做判断，增强代码的健壮性
        if (filter.getTagIds() != null) {
            tagListQuery.in(true, "authList", filter.getTagIds());
        }
        // 3.2 因为前端的小Bug，传递的数据有问题，在这里提前做判断，增强代码的健壮性
        if (
                params.getCreatedTime() != null
                        &&
                        !("{".equals(params.getCreatedTime().getStartTime()))
                        &&
                        !("\"".equals(params.getCreatedTime().getEndTime()))
        ) {
            tagListQuery
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
            tagListQuery
                    .between(
                            true,
                            "modifiedTime",
                            params.getModifiedTime().getStartTime(),
                            params.getModifiedTime().getEndTime()
                    );
        }

        // 附加：赋默认值
        if (page.getCurrent() == 0) {
            page.setCurrent(1);
        }
        if (page.getPageSize() == 0) {
            page.setPageSize(10);
        }

        // 4. 分页查询
        Page<Tag> tagPageC = new Page<>(page.getCurrent(), page.getPageSize());
        // 4.1 查询未分页时的数据总数
        List<Tag> tagNotPage = tagMapper.selectList(tagListQuery);
        // 4.2 查询分页后的数据
        Page<Tag> tagPage = tagMapper.selectPage(tagPageC, tagListQuery);

        log.info("current: {}", page.getCurrent());
        log.info("pageSize: {}", page.getPageSize());
        log.info("total: {}", tagNotPage.size());
        log.info("tags: {}", tagPage);

        // 5. 封装数据
        ListResDTO<Tag> tagListRes = new ListResDTO<>();
        tagListRes.setCurrent(page.getCurrent());
        tagListRes.setPageSize(page.getPageSize());
        tagListRes.setList(tagPage.getRecords());
        tagListRes.setTotal(tagNotPage.size());
        return Result.success("获取成功", tagListRes);
    }
}




