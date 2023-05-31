package top.kkuily.xingbackend.model.vo;

import lombok.Data;

/**
 * @author 小K
 */
@Data
public class ListPageVO {
    /**
     * 当前页
     */
    private int current;
    /**
     * 当前数据条数
     */
    private int pageSize;
}
