package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kkuily.xingbackend.model.po.Location;
import top.kkuily.xingbackend.service.ILocationService;
import top.kkuily.xingbackend.mapper.LocationMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【location】的数据库操作Service实现
* @createDate 2023-05-21 13:05:14
*/
@Service
public class LocationServiceImpl extends ServiceImpl<LocationMapper, Location>
    implements ILocationService {

}




