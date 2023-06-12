package top.kkuily.xingbackend.model.dto.request.tag;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.kkuily.xingbackend.model.po.Tag;

/**
 * @author 小K
 * @description 更新标签的传输数据
 */
@Data
public class TagUpdateBodyDTO {

    /**
     * 标签ID
     */
    private String id;

    /**
     * 标签名
     */
    private String tagName;

    /**
     * @param tag Tag
     */
    public void convertTo(Tag tag) {
        BeanUtils.copyProperties(this, tag);
    }
}
