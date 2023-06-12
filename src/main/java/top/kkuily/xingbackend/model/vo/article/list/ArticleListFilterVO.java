package top.kkuily.xingbackend.model.vo.article.list;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 小K
 * @description 标签过滤参数
 */
@Data
public class ArticleListFilterVO implements Serializable {

    /**
     * 分类信息(例如：1, 2, 3)
     */
    private String categoryIds;

    /**
     * 状态信息(例如：1, 2, 3)
     */
    private String statusIds;

    @Serial
    private static final long serialVersionUID = 1L;
}
