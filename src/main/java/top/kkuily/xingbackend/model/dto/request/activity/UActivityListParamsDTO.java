package top.kkuily.xingbackend.model.dto.request.activity;

import lombok.Data;

/**
 * @author 小K
 * @description 分页请求活动的DTO类
 */
@Data
public class UActivityListParamsDTO {
    /**
     * 活动ID
     */
    private String activityId;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 当前页大小
     */
    private Integer pageSize;
}
