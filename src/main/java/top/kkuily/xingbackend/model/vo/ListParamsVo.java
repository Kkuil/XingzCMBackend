package top.kkuily.xingbackend.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 小K
 * @description 管理员分页查询请求参数
 */
@Data
@AllArgsConstructor
public class ListParamsVo<ListParamsType, ListSortType, ListFilterType> {
    private ListParamsType params;
    private ListSortType sort;
    private ListFilterType filter;
    private ListPageVo page;
}
