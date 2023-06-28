package top.kkuily.xingbackend.web.controller.admin;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.kkuily.xingbackend.anotation.AdminAuthToken;
import top.kkuily.xingbackend.model.dto.request.article.admin.ArticleAddBodyDTO;
import top.kkuily.xingbackend.model.dto.request.article.admin.ArticleUpdateBodyDTO;
import top.kkuily.xingbackend.model.enums.ArticleCategoryEnums;
import top.kkuily.xingbackend.model.enums.ArticleStatusEnums;
import top.kkuily.xingbackend.model.enums.IsDeletedEnums;
import top.kkuily.xingbackend.model.po.Article;
import top.kkuily.xingbackend.model.po.ArticleStatus;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.article.list.ArticleListFilterVO;
import top.kkuily.xingbackend.model.vo.article.list.ArticleListParamsVO;
import top.kkuily.xingbackend.model.vo.article.list.ArticleListSortVO;
import top.kkuily.xingbackend.service.*;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.ValidateUtils;
import top.kkuily.xingbackend.model.po.ArticleCategory;
import top.kkuily.xingbackend.model.po.ArticleTag;

import static top.kkuily.xingbackend.constant.aritcle.ArticleInfo.*;
import static top.kkuily.xingbackend.constant.commons.Global.SPLITOR;

/**
 * @author 小K
 * @description 文章相关接口
 */
@RestController
@Slf4j
public class ArticleController {

    @Resource
    private IArticleService articleService;

    @Resource
    private IArticleStatusService articleStatusService;

    @Resource
    private IArticleCategoryService articleCategoryService;

    @Resource
    private IArticleTagService articleTagService;

    /**
     * @param params ArticleListParamsVO
     * @param sort   ArticleListSortVO
     * @param filter ArticleListFilterVO
     * @param page   ArticleListPageVO
     * @return Result
     * @description 文章分页查询接口
     */
    @GetMapping("article")
    public Result getList(String params, String sort, String filter, String page) {
        log.info("page: {}", params);
        ArticleListParamsVO paramsBean = JSONUtil.toBean(params, ArticleListParamsVO.class);
        ArticleListSortVO sortBean = JSONUtil.toBean(sort, ArticleListSortVO.class);
        ArticleListFilterVO filterBean = JSONUtil.toBean(filter, ArticleListFilterVO.class);
        ListPageVO pageBean = JSONUtil.toBean(page, ListPageVO.class);
        ListParamsVO<ArticleListParamsVO, ArticleListSortVO, ArticleListFilterVO> listParams = new ListParamsVO<>(paramsBean, sortBean, filterBean, pageBean);
        return articleService.adminGetList(listParams);
    }

    // region
    // 增删改查

    /**
     * @param articleAddBodyDTO AuthAddBodyDTO
     * @return Result
     * @description 增
     */
    @PostMapping("article")
    @AdminAuthToken
    @Transactional(rollbackFor = Exception.class)
    public Result add(@RequestBody ArticleAddBodyDTO articleAddBodyDTO) {
        try {
            // 判空
            ValidateUtils.validateNotEmpty("标题", articleAddBodyDTO.getTitle());
            ValidateUtils.validateNotEmpty("内容", articleAddBodyDTO.getContent());
            // 判长
            ValidateUtils.validateLength("标题", articleAddBodyDTO.getTitle(), ARTICLE_TITLE_LEN_RANGE[0], ARTICLE_TITLE_LEN_RANGE[1]);
            ValidateUtils.validateLength("内容", articleAddBodyDTO.getContent(), ARTICLE_CONTENT_LEN_RANGE[0], ARTICLE_CONTENT_LEN_RANGE[1]);
            if (!StringUtils.isEmpty(articleAddBodyDTO.getTagIds()) && articleAddBodyDTO.getTagIds().split(SPLITOR).length > ARTICLE_MAX_TAG_COUNT) {
                throw new IllegalArgumentException("标签数量过多，请选择合适数量的标签");
            }
            // 查询statusId是否存在
            Integer statusId = articleAddBodyDTO.getStatusId();
            if (statusId != null) {
                ArticleStatus articleStatus = articleStatusService.getById(statusId);
                if (articleStatus == null) {
                    throw new IllegalArgumentException("状态不存在，非法操作");
                }
            }
        } catch (Exception e) {
            return Result.fail(403, e.getMessage(), MsgType.ERROR_MESSAGE);
        }
        // 保存Article数据
        Article article = new Article();
        articleAddBodyDTO.convertTo(article);
        String articleId = UUID.randomUUID().toString();
        article.setId(articleId);
        boolean isArticleSave = articleService.save(article);

        // 保存ArticleTag数据
        String[] tags = articleAddBodyDTO.getTagIds().split(SPLITOR);
        for (String tag : tags) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setId(articleId);
            articleTag.setTagId(Integer.valueOf(tag));
            articleTagService.save(articleTag);
        }

        if (isArticleSave) {
            return Result.success("文章发布成功", true);
        } else {
            return Result.fail(500, "文章发布失败", MsgType.ERROR_MESSAGE);
        }
    }

    /**
     * @param id String
     * @return Result
     * @description 删
     */
    @DeleteMapping("article")
    @AdminAuthToken
    public Result del(String id) {
        // 1. 判断id是否存在
        Article articleInTable = articleService.getById(id);
        if (articleInTable == null) {
            return Result.fail(403, "文章不存在", MsgType.ERROR_MESSAGE);
        }
        // 2. 判断是否删除
        boolean isDel = articleService.removeById(id);
        if (isDel) {
            return Result.success("删除成功", true);
        } else {
            return Result.fail(403, "删除失败", MsgType.ERROR_MESSAGE);
        }
    }

    /**
     * @param articleUpdateBodyDTO AuthUpdateBodyDTO
     * @return Result
     * @description 改
     */
    @PutMapping("article")
    @AdminAuthToken
    public Result update(@RequestBody ArticleUpdateBodyDTO articleUpdateBodyDTO) {
        return Result.success("更新成功", true);
    }

    /**
     * @param id String
     * @return Result
     * @description 获取某个文章
     */
    @GetMapping("/article/{id}")
    @AdminAuthToken
    public Result get(@PathVariable("id") String id) {
        // 1. 判断账号是否存在
        Article articleInTable = articleService.getById(id);
        if (articleInTable == null) {
            return Result.fail(403, "文章不存在", MsgType.ERROR_MESSAGE);
        }
        // 2. 判断是否被逻辑删除
        String isDeleted = articleInTable.getIsDeleted();
        if (IsDeletedEnums.NO.getValue().equals(isDeleted)) {
            return Result.success("获取成功", true);
        } else {
            return Result.success("获取成功，该文章已被删除", true);
        }
    }

    // endregion

    /**
     * @param id String
     * @return Result
     * @description 通过审核
     */
    @PutMapping("/article/approved/{id}")
    public Result approved(@PathVariable("id") String id) {
        // 判断文章是否存在
        Article article = articleService.getById(id);
        if (article == null) {
            return Result.fail(403, "该文章不存在，请确认文章ID是否准确", MsgType.ERROR_MESSAGE);
        }
        // 判断该文章的状态是不是已经是已审核状态
        Integer statusId = article.getStatusId();
        if (statusId == ArticleStatusEnums.AUDITED.getValue()) {
            return Result.fail(403, "该文章的状态已为已审核状态，请勿重复操作", MsgType.WARN_MESSAGE);
        }
        // 修改文状态为已审核
        Article articleEntity = new Article();
        articleEntity.setId(id);
        articleEntity.setStatusId(ArticleStatusEnums.AUDITED.getValue());
        boolean isUpdate = articleService.updateById(articleEntity);
        if (isUpdate) {
            return Result.success("已审核", true, MsgType.NOTIFICATION);
        } else {
            return Result.fail(403, "审核失败", MsgType.ERROR_MESSAGE);
        }
    }

    /**
     * @param id String
     * @return Result
     * @description 驳回
     */
    @PutMapping("/article/rejected/{id}")
    public Result rejected(@PathVariable("id") String id) {
        // 判断文章是否存在
        Article article = articleService.getById(id);
        if (article == null) {
            return Result.fail(403, "该文章不存在，请确认文章ID是否准确", MsgType.ERROR_MESSAGE);
        }
        // 判断该文章的状态是不是已经是已驳回状态
        Integer statusId = article.getStatusId();
        if (statusId == ArticleStatusEnums.REJECTED.getValue()) {
            return Result.fail(403, "该文章的状态已为已驳回状态，请勿重复操作", MsgType.WARN_MESSAGE);
        }
        // 修改文状态为已驳回
        Article articleEntity = new Article();
        articleEntity.setId(id);
        articleEntity.setStatusId(ArticleStatusEnums.REJECTED.getValue());
        boolean isUpdate = articleService.updateById(articleEntity);
        if (isUpdate) {
            return Result.success("已驳回", true, MsgType.NOTIFICATION);
        } else {
            return Result.fail(403, "驳回失败", MsgType.ERROR_MESSAGE);
        }
    }

    /**
     * @param id String
     * @return Result
     * @description 下架
     */
    @PutMapping("/article/undercarriage/{id}")
    public Result undercarriage(@PathVariable("id") String id) {
        // 判断文章是否存在
        Article article = articleService.getById(id);
        if (article == null) {
            return Result.fail(403, "该文章不存在，请确认文章ID是否准确", MsgType.ERROR_MESSAGE);
        }
        // 判断该文章的状态是不是已经是已下架状态
        Integer statusId = article.getStatusId();
        if (statusId == ArticleStatusEnums.UNDERCARRIAGE.getValue()) {
            return Result.fail(403, "该文章的状态已为已下架状态，请勿重复操作", MsgType.WARN_MESSAGE);
        }
        // 修改文状态为已下架
        Article articleEntity = new Article();
        articleEntity.setId(id);
        articleEntity.setStatusId(ArticleStatusEnums.UNDERCARRIAGE.getValue());
        boolean isUpdate = articleService.updateById(articleEntity);
        if (isUpdate) {
            return Result.success("已下架", true, MsgType.NOTIFICATION);
        } else {
            return Result.fail(403, "下架失败", MsgType.ERROR_MESSAGE);
        }
    }

    /**
     * @param id String
     * @return Result
     * @description 下架
     */
    @PutMapping("/article/hot/{id}")
    public Result hot(@PathVariable("id") String id) {
        // 判断文章是否存在
        Article article = articleService.getById(id);
        if (article == null) {
            return Result.fail(403, "该文章不存在，请确认文章ID是否准确", MsgType.ERROR_MESSAGE);
        }
        // 判断该文章的状态是不是已经是已热门状态
        QueryWrapper<ArticleCategory> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper
                .eq("id", id)
                .eq("categoryId", ArticleCategoryEnums.HOT);
        ArticleCategory ac = articleCategoryService.getOne(articleQueryWrapper);

        if (ac != null) {
            return Result.fail(403, "该文章分类已为已热门，请勿重复操作", MsgType.WARN_MESSAGE);
        }

        // 修改文状态为已热门
        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setId(id);
        articleCategory.setCategoryId(String.valueOf(ArticleCategoryEnums.HOT));
        boolean isUpdate = articleCategoryService.updateById(articleCategory);
        if (isUpdate) {
            return Result.success("已热门", true, MsgType.NOTIFICATION);
        } else {
            return Result.fail(403, "热门失败", MsgType.ERROR_MESSAGE);
        }
    }

    /**
     * @param id String
     * @return Result
     * @description 下架
     */
    @PutMapping("/article/high-quality/{id}")
    public Result highQuality(@PathVariable("id") String id) {
        // 判断文章是否存在
        Article article = articleService.getById(id);
        if (article == null) {
            return Result.fail(403, "该文章不存在，请确认文章ID是否准确", MsgType.ERROR_MESSAGE);
        }
        // 判断该文章的状态是不是已经是已优质状态
        QueryWrapper<ArticleCategory> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper
                .eq("id", id)
                .eq("categoryId", ArticleCategoryEnums.HIGH_QUALITY);
        ArticleCategory ac = articleCategoryService.getOne(articleQueryWrapper);

        if (ac != null) {
            return Result.fail(403, "该文章分类已为已优质，请勿重复操作", MsgType.WARN_MESSAGE);
        }

        // 修改文状态为已优质
        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setId(id);
        articleCategory.setCategoryId(String.valueOf(ArticleCategoryEnums.HIGH_QUALITY));
        boolean isUpdate = articleCategoryService.updateById(articleCategory);
        if (isUpdate) {
            return Result.success("已优质", true, MsgType.NOTIFICATION);
        } else {
            return Result.fail(403, "优质失败", MsgType.ERROR_MESSAGE);
        }
    }
}
