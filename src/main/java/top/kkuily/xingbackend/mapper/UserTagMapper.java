package top.kkuily.xingbackend.mapper;

import org.springframework.web.bind.annotation.PathVariable;
import top.kkuily.xingbackend.model.po.UserTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.HashMap;
import java.util.List;

/**
 * @author 小K
 * @description 针对表【user_tag】的数据库操作Mapper
 * @createDate 2023-06-15 18:29:18
 * @Entity top.kkuily.xingbackend.model.po.UserTag
 */
public interface UserTagMapper extends BaseMapper<UserTag> {

    /**
     * @param userId String
     * @return List<Integer>
     * @description 通过用户ID获取对应的标签IDS
     */
    List<Integer> selectTagIdsById(@PathVariable("userId") String userId);

    /**
     * @param userId String
     * @description 通过用户ID删除对应的标签IDS
     */
    boolean deleteTagIdsById(@PathVariable("userId") String userId);

    /**
     * @param id String
     * @return List<String>
     * @description 通过用户ID获取对应的标签
     */
    List<String> selectTagsByUserId(String id);
}




