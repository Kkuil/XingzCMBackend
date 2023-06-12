package top.kkuily.xingbackend.service;

import top.kkuily.xingbackend.model.po.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.tag.list.TagListFilterVO;
import top.kkuily.xingbackend.model.vo.tag.list.TagListParamsVO;
import top.kkuily.xingbackend.model.vo.tag.list.TagListSortVO;
import top.kkuily.xingbackend.utils.Result;

/**
* @author 小K
* @description 针对表【tag】的数据库操作Service
* @createDate 2023-05-18 11:21:27
*/
public interface ITagService extends IService<Tag> {

    Result getList(ListParamsVO<TagListParamsVO, TagListSortVO, TagListFilterVO> listParams);
}
