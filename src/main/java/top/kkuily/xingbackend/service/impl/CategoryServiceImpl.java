package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.kkuily.xingbackend.model.po.Category;
import top.kkuily.xingbackend.service.ICategoryService;
import top.kkuily.xingbackend.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 小K
* @description 针对表【article_category】的数据库操作Service实现
* @createDate 2023-06-12 19:59:27
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements ICategoryService {

}




