package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import top.kkuily.xingbackend.mapper.AuthMapper;
import top.kkuily.xingbackend.model.dto.response.ListResDTO;
import top.kkuily.xingbackend.model.dto.response.role.RoleInfoResDTO;
import top.kkuily.xingbackend.model.po.Role;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.role.list.RoleListFilterVO;
import top.kkuily.xingbackend.model.vo.role.list.RoleListParamsVO;
import top.kkuily.xingbackend.model.vo.role.list.RoleListSortVO;
import top.kkuily.xingbackend.service.IRoleService;
import top.kkuily.xingbackend.mapper.RoleMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.utils.Result;

import java.util.Arrays;
import java.util.List;

import static top.kkuily.xingbackend.constant.commons.Global.SPLITOR;

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
    private AuthMapper authMapper;

    /**
     * @param roleListParams ListParamsVO
     * @return Result
     * @author 小K
     * @description 分页查询
     */
    @Override
    public Result getList(ListParamsVO<RoleListParamsVO, RoleListSortVO, RoleListFilterVO> roleListParams) {
        // 获取数据
        RoleListParamsVO params = roleListParams.getParams();
        RoleListSortVO sort = roleListParams.getSort();
        RoleListFilterVO filter = roleListParams.getFilter();
        ListPageVO page = roleListParams.getPage();

        // 分页数据处理
        ListPageVO listPageVO = new ListPageVO();
        listPageVO.setCurrent((page.getCurrent() - 1) * page.getPageSize());
        listPageVO.setPageSize(page.getPageSize());
        roleListParams.setPage(listPageVO);

        // 查询数据
        List<RoleInfoResDTO> roleInfoResDTO = null;
        // 总条数
        int total = 0;
        try {
            roleInfoResDTO = roleMapper.listRolesWithLimit(params, sort, filter, listPageVO);
            // 通过authIds查询auths
            for (int i = 0; i < roleInfoResDTO.size(); i++) {
                String authIds = roleInfoResDTO.get(i).getAuthIds();
                if (authIds != null) {
                    List<String> idsList = Arrays.asList(authIds.split(SPLITOR));
                    List<String> auths = authMapper.selectAuthDescriptionListByBatchId(idsList);
                    roleInfoResDTO.get(i).setAuthList(
                            auths.toString()
                                    .replace("[", "")
                                    .replace("]", "")
                    );
                }
            }
            total = roleMapper.listRolesWithNotLimit(params, sort, filter, listPageVO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 封装数据
        ListResDTO<RoleInfoResDTO> roleListRes = new ListResDTO<>();
        roleListRes.setCurrent(page.getCurrent());
        roleListRes.setPageSize(page.getPageSize());
        roleListRes.setList(roleInfoResDTO);
        roleListRes.setTotal(total);

        return Result.success("获取成功", roleListRes);
    }
}




