package top.kkuily.xingbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.kkuily.xingbackend.model.dto.response.user_rank.UserRankInfoResDTO;
import top.kkuily.xingbackend.model.po.UserRank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 小K
* @description 针对表【user_rank】的数据库操作Mapper
* @createDate 2023-05-18 11:22:01
* @Entity top.kkuily.xingbackend.model.entity.UserRank
*/
@Mapper
public interface UserRankMapper extends BaseMapper<UserRank> {

    /**
     * @description 获取用户等级情况
     * @param current int
     * @param pageSize int
     * @return List<UserRankInfoResDTO>
     */
    List<UserRankInfoResDTO> selectPageAndUserInfoWithLimit(int offset, int pageSize, int sort);

    /**
     * @description 获取用户等级情况(不带分页)
     * @return int
     */
    int selectPageAndUserInfoWithNotLimit(int sort);
}




