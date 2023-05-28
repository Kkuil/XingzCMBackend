package top.kkuily.xingbackend.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 小K
 * @description 时间范围
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateRangeCommonVo {
    private String startTime;
    private String endTime;
}
