package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kkuily.xingbackend.model.po.Department;
import top.kkuily.xingbackend.service.IDepartmentService;
import top.kkuily.xingbackend.mapper.DepartmentMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【department】的数据库操作Service实现
* @createDate 2023-05-21 13:03:51
*/
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
    implements IDepartmentService {

}




