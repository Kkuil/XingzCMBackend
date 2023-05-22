package top.kkuily.xingbackend.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 小K
 * @description 管理员分页查询请求参数
 */
@Data
@AllArgsConstructor
public class ListParams {
    private Object params;
    private Object sort;
    private Object filter;
}
