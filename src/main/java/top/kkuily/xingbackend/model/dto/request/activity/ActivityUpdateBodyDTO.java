package top.kkuily.xingbackend.model.dto.request.activity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.kkuily.xingbackend.model.po.Role;

import java.util.Date;

/**
 * @author 小K
 * @description 更新角色的传输数据
 */
@Data
public class ActivityUpdateBodyDTO {
    /**
     * 活动发起者ID
     */
    private String activatorId;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动详情
     */
    private String detail;

    /**
     * 活动封面
     */
    private String cover;

    /**
     * 活动类型
     */
    private String type;

    /**
     * 活动规则
     */
    private String rule;

    /**
     * 备注
     */
    private String ps;

    /**
     * 活动开始时间
     */
    private Date startTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 参与人数上限
     */
    private Integer maxAttendance;

    /**
     * 谁可参与，1为全部，2为会员
     */
    private Integer authorizedPersonnel;

    /**
     * 最大可获得星分币数，-1为无上限
     */
    private Integer maxAquirableXingCoin;
}
