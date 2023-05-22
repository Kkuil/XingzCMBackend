package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kkuily.xingbackend.model.po.UserRank;
import top.kkuily.xingbackend.service.IUserRankService;
import top.kkuily.xingbackend.mapper.UserRankMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【user_rank】的数据库操作Service实现
* @createDate 2023-05-18 11:22:01
*/
@Service
public class UserRankServiceImpl extends ServiceImpl<UserRankMapper, UserRank>
    implements IUserRankService {

}




