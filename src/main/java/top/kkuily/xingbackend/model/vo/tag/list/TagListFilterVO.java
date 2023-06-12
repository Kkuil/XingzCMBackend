package top.kkuily.xingbackend.model.vo.tag.list;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 小K
 * @description 标签过滤参数
 */
@Data
public class TagListFilterVO implements Serializable {
    /**
     * 权限列表
     */
    private String[] tagIds;

    @Serial
    private static final long serialVersionUID = 1L;
}
