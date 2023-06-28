package top.kkuily.xingbackend.model.dto.response;

import lombok.Data;

import java.util.List;

/**
 * @author 小K
 * @description Admin 分页查询响应参数
 */
@Data
public class ListResDTO<DataType> {
    private List<DataType> list;
    private Integer current;
    private Integer pageSize;
    private Integer total;

}
