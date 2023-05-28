package top.kkuily.xingbackend.model.vo.user.list;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author 小K
 * @description 管理员过滤参数
 */
@Data
public class UserListFilterVo {

    /**
     * 性别（0：女 1：男 2：未知）
     */
    private String[] gender;

    /**
     * 标签（例如：['前端'，'后端']）
     */
    private String[] tagIds;

    /**
     * 是否为VIP用户（0：非会员 1：会员）
     */
    private String[] isVip;

    /**
     * 是否逻辑删除(0：未删除 1：已删除)
     */

    private String[] isDeleted;
}
