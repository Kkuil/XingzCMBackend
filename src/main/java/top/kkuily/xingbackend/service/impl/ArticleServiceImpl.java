package top.kkuily.xingbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import top.kkuily.xingbackend.mapper.TagMapper;
import top.kkuily.xingbackend.model.dto.response.ListResDTO;
import top.kkuily.xingbackend.model.dto.response.article.ArticleInfoResDTO;
import top.kkuily.xingbackend.model.po.Article;
import top.kkuily.xingbackend.mapper.ArticleMapper;
import org.springframework.stereotype.Service;
import top.kkuily.xingbackend.model.po.Tag;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.article.list.ArticleListFilterVO;
import top.kkuily.xingbackend.model.vo.article.list.ArticleListParamsVO;
import top.kkuily.xingbackend.model.vo.article.list.ArticleListSortVO;
import top.kkuily.xingbackend.service.IArticleService;
import top.kkuily.xingbackend.utils.Result;

import java.util.List;

/**
 * @author 小K
 * @description 针对表【article】的数据库操作Service实现
 * @createDate 2023-06-08 10:16:55
 */
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements IArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private TagMapper tagMapper;

    /**
     * @param articleListParams
     * @return
     * @description 文章的分页查询
     */
    @Override
    public Result getList(ListParamsVO<ArticleListParamsVO, ArticleListSortVO, ArticleListFilterVO> articleListParams) {
        // 获取数据
        ArticleListParamsVO params = articleListParams.getParams();
        ArticleListSortVO sort = articleListParams.getSort();
        ArticleListFilterVO filter = articleListParams.getFilter();
        ListPageVO page = articleListParams.getPage();

        // 分页数据处理
        ListPageVO listPageVO = new ListPageVO();
        listPageVO.setCurrent((page.getCurrent() - 1) * page.getPageSize());
        listPageVO.setPageSize(page.getPageSize());
        articleListParams.setPage(listPageVO);

        // 查询数据
        List<ArticleInfoResDTO> articleInfoResDTO = null;
        // 总条数
        int total = 0;
        try {
            articleInfoResDTO = articleMapper.listArticlesWithLimit(params, sort, filter, listPageVO);
            total = articleMapper.listArticlesWithNotLimit(params, sort, filter, listPageVO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 封装数据
        ListResDTO<ArticleInfoResDTO> articleListRes = new ListResDTO<>();
        articleListRes.setCurrent(page.getCurrent());
        articleListRes.setPageSize(page.getPageSize());
        articleListRes.setList(articleInfoResDTO);
        articleListRes.setTotal(total);

        return Result.success("获取成功", articleListRes);
    }
}
