package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kkuily.xingbackend.model.po.UserActivity;
import top.kkuily.xingbackend.service.IUserActivityService;
import top.kkuily.xingbackend.mapper.UserActivityMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【user_activity】的数据库操作Service实现
* @createDate 2023-07-03 14:42:33
*/
@Service
public class UserActivityServiceImpl extends ServiceImpl<UserActivityMapper, UserActivity>
    implements IUserActivityService {

}




