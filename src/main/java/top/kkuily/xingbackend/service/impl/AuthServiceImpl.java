package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kkuily.xingbackend.model.po.Auth;
import top.kkuily.xingbackend.service.IAuthService;
import top.kkuily.xingbackend.mapper.AuthMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【auth】的数据库操作Service实现
* @createDate 2023-05-31 11:00:28
*/
@Service
public class AuthServiceImpl extends ServiceImpl<AuthMapper, Auth>
    implements IAuthService {

}




