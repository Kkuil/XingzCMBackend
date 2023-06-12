package top.kkuily.xingbackend.model.dto.request.tag;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.kkuily.xingbackend.model.po.Tag;

/**
 * @author 小K
 * @description 增加角色的DTO类
 */
@Data
public class TagAddBodyDTO {
    /**
     * 标签名
     */
    private String tagName;

    /**
     * 转换为本类方法
     *
     * @param tag Tag
     */
    public void convertTo(Tag tag) {
        BeanUtils.copyProperties(this, tag);
    }
}
