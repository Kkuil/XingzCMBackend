package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kkuily.xingbackend.model.po.Tag;
import top.kkuily.xingbackend.service.ITagService;
import top.kkuily.xingbackend.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【tag】的数据库操作Service实现
* @createDate 2023-05-18 11:21:27
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements ITagService {

}




