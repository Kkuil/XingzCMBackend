package top.kkuily.xingbackend.model.dto.response.admin;

import lombok.Data;
import top.kkuily.xingbackend.model.po.Admin;

import java.util.List;

/**
 * @author 小K
 * @description Admin 分页查询响应参数
 */
@Data
public class AdminListRes {
    private List<Admin> list;
    private int current;
    private int pageSize;
    private int total;
}
