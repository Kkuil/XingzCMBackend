package top.kkuily.xingbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.kkuily.xingbackend.model.po.Article;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.article.list.ArticleListFilterVO;
import top.kkuily.xingbackend.model.vo.article.list.ArticleListParamsVO;
import top.kkuily.xingbackend.model.vo.article.list.ArticleListSortVO;
import top.kkuily.xingbackend.utils.Result;

/**
* @author 小K
* @description 针对表【article】的数据库操作Service
* @createDate 2023-05-30 11:50:08
*/
public interface IArticleService extends IService<Article> {

    Result getList(ListParamsVO<ArticleListParamsVO, ArticleListSortVO, ArticleListFilterVO> listParams);
}
