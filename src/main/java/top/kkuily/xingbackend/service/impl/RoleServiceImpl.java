package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kkuily.xingbackend.model.po.Role;
import top.kkuily.xingbackend.service.IRoleService;
import top.kkuily.xingbackend.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【role】的数据库操作Service实现
* @createDate 2023-05-21 16:12:27
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements IRoleService {

}




