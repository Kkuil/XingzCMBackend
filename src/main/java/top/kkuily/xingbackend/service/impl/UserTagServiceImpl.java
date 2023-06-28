package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kkuily.xingbackend.model.po.UserTag;
import top.kkuily.xingbackend.service.IUserTagService;
import top.kkuily.xingbackend.mapper.UserTagMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【user_tag】的数据库操作Service实现
* @createDate 2023-06-15 18:29:18
*/
@Service
public class UserTagServiceImpl extends ServiceImpl<UserTagMapper, UserTag>
    implements IUserTagService {

}




