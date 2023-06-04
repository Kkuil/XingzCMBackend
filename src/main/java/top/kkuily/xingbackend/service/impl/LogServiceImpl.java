package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kkuily.xingbackend.model.po.Log;
import top.kkuily.xingbackend.service.ILogService;
import top.kkuily.xingbackend.mapper.LogMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【log】的数据库操作Service实现
* @createDate 2023-06-04 15:26:08
*/
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log>
    implements ILogService {

}




