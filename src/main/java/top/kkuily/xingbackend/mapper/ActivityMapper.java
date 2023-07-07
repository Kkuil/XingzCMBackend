package top.kkuily.xingbackend.mapper;

import top.kkuily.xingbackend.model.dto.response.activity.ActivityInfoResWithUserDTO;
import top.kkuily.xingbackend.model.dto.response.article.user.ArticleInfoResWithUserDTO;
import top.kkuily.xingbackend.model.po.Activity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author 小K
 * @description 针对表【activity】的数据库操作Mapper
 * @createDate 2023-07-03 13:53:13
 * @Entity top.kkuily.xingbackend.model.po.Activity
 */
public interface ActivityMapper extends BaseMapper<Activity> {
    /**
     * @param activityId String
     * @param current    int
     * @param pageSize   int
     * @return List<ActivityInfoResWithUserDTO>
     */
    List<ActivityInfoResWithUserDTO> listActivitiesWithLimitAndUser(String activityId, Integer current, Integer pageSize);

    /**
     * @param activityId      String
     * @return Integer
     */
    int listActivitiesWithNotLimitAndUser(String activityId);
}




