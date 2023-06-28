package top.kkuily.xingbackend.service;

import top.kkuily.xingbackend.model.po.UserRank;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kkuily.xingbackend.utils.Result;

/**
 * @author 小K
 * @description 针对表【user_rank】的数据库操作Service
 * @createDate 2023-05-18 11:22:01
 */
public interface IUserRankService extends IService<UserRank> {

    /**
     * @param current  int
     * @param pageSize int
     * @return Result
     * @description 获取用户等级情况
     */
    Result listUserRank(int current, int pageSize, int sort);
}
