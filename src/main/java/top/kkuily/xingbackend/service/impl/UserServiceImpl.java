package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kkuily.xingbackend.model.po.User;
import top.kkuily.xingbackend.service.IUserService;
import top.kkuily.xingbackend.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-05-18 11:21:38
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements IUserService {

}




