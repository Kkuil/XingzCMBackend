package top.kkuily.xingbackend.model.dto.request.commons;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 小K
 * @description 管理员分页查询请求参数
 */
@Data
@AllArgsConstructor
public class ListParams {
    private String params;
    private String sort;
    private String filter;
}
