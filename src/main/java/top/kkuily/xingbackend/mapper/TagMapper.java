package top.kkuily.xingbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.kkuily.xingbackend.model.po.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author 小K
 * @description 针对表【tag】的数据库操作Mapper
 * @createDate 2023-05-18 11:21:27
 * @Entity top.kkuily.xingbackend.model.entity.Tag
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * @param ids String
     * @return List<String>
     * @description 通过多个标签id查询标签名
     */
    List<String> findTagNameListByBatchId(@Param("ids") List<String> ids);
}




