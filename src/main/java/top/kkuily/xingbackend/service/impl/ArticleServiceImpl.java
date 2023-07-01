package top.kkuily.xingbackend.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.mapper.*;
import top.kkuily.xingbackend.model.bo.Article.ArticleLinkBO;
import top.kkuily.xingbackend.model.dto.request.article.admin.ArticleAddBodyDTO;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleListParamsDTO;
import top.kkuily.xingbackend.model.dto.request.article.user.UArticleListParamsWithUserIdDTO;
import top.kkuily.xingbackend.model.dto.response.ListResDTO;
import top.kkuily.xingbackend.model.dto.response.article.admin.ArticleInfoResWithAdminDTO;
import top.kkuily.xingbackend.model.dto.response.article.user.ArticleDetailInfoResWithUserDTO;
import top.kkuily.xingbackend.model.dto.response.article.user.ArticleInfoResWithUserDTO;
import top.kkuily.xingbackend.model.enums.ArticleStatusEnums;
import top.kkuily.xingbackend.model.enums.SortedTypeEnums;
import top.kkuily.xingbackend.model.enums.UserArticleTypeEnums;
import top.kkuily.xingbackend.model.po.Article;
import top.kkuily.xingbackend.model.po.ArticleTag;
import top.kkuily.xingbackend.model.po.UserArticle;
import top.kkuily.xingbackend.model.vo.ListPageVO;
import top.kkuily.xingbackend.model.vo.ListParamsVO;
import top.kkuily.xingbackend.model.vo.article.detail.ArticleDetailInfoVO;
import top.kkuily.xingbackend.model.vo.article.detail.AuthorArticleDetailInfoVO;
import top.kkuily.xingbackend.model.vo.article.detail.AuthorDetailInfoVO;
import top.kkuily.xingbackend.model.vo.article.detail.CommentDetailInfoVO;
import top.kkuily.xingbackend.model.vo.article.list.ArticleListFilterVO;
import top.kkuily.xingbackend.model.vo.article.list.ArticleListParamsVO;
import top.kkuily.xingbackend.model.vo.article.list.ArticleListSortVO;
import top.kkuily.xingbackend.service.IArticleService;
import top.kkuily.xingbackend.utils.Result;
import top.kkuily.xingbackend.utils.Token;
import top.kkuily.xingbackend.utils.ValidateUtils;

import java.util.List;
import java.util.Objects;

import static top.kkuily.xingbackend.constant.aritcle.ArticleInfo.*;
import static top.kkuily.xingbackend.constant.commons.Global.MAX_COUNT_PER_LIST;
import static top.kkuily.xingbackend.constant.commons.Global.SPLITOR;
import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_KEY_IN_HEADER;
import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_SECRET;

/**
 * @author 小K
 * @description 针对表【article】的数据库操作Service实现
 * @createDate 2023-06-08 10:16:55
 */
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleTagMapper articleTagMapper;

    @Resource
    private UserTagMapper userTagMapper;

    @Resource
    private ArticleCommentMapper articleCommentMapper;

    @Resource
    private UserArticleMapper userArticleMapper;

    /**
     * @param articleAddBodyDTO ArticleAddBodyDTO
     * @param request           HttpServletRequest
     * @return Result
     * @description 发布文章
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result publish(ArticleAddBodyDTO articleAddBodyDTO, HttpServletRequest request) {
        try {
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
            } catch (Exception e) {
                return Result.fail(403, e.getMessage(), MsgType.ERROR_MESSAGE);
            }

            // 获取用户ID
            String token = request.getHeader(USER_TOKEN_KEY_IN_HEADER);
            Claims parseToken;
            try {
                parseToken = Token.parse(token, USER_TOKEN_SECRET);
            } catch (Exception e) {
                return Result.fail(403, "验证异常，请检查是否登录或登录是否过期", MsgType.ERROR_MESSAGE);
            }
            String userId = (String) parseToken.get("id");

            // 保存Article数据
            Article article = new Article();
            articleAddBodyDTO.convertTo(article);
            String articleId = UUID.randomUUID().toString().substring(0, 32);
            article.setId(articleId);
            article.setUserId(userId);
            int isArticleSave = articleMapper.insert(article);

            // 保存ArticleTag数据
            if (!StringUtils.isEmpty(articleAddBodyDTO.getTagIds())) {
                for (String tag : articleAddBodyDTO.getTagIds().split(SPLITOR)) {
                    ArticleTag articleTag = new ArticleTag();
                    articleTag.setId(articleId);
                    articleTag.setTagId(Integer.valueOf(tag));
                    articleTagMapper.insert(articleTag);
                }
            }

            if (isArticleSave == 1) {
                return Result.success("文章发布成功，待审核", true);
            } else {
                return Result.fail(500, "文章发布失败", MsgType.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            // 显式回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail(403, "文章发布失败", MsgType.ERROR_MESSAGE);
        }
    }

    /**
     * @param articleAddBodyDTO ArticleAddBodyDTO
     * @param request           HttpServletRequest
     * @return Result
     * @description 保存草稿
     */
    @Override
    public Result saveDraft(ArticleAddBodyDTO articleAddBodyDTO, HttpServletRequest request) {
        try {
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
            } catch (Exception e) {
                return Result.fail(403, e.getMessage(), MsgType.ERROR_MESSAGE);
            }

            // 获取用户ID
            String token = request.getHeader(USER_TOKEN_KEY_IN_HEADER);
            Claims parseToken;
            try {
                parseToken = Token.parse(token, USER_TOKEN_SECRET);
            } catch (Exception e) {
                return Result.fail(403, "验证异常，请检查是否登录或登录是否过期", MsgType.ERROR_MESSAGE);
            }
            String userId = (String) parseToken.get("id");

            // 保存Article数据
            Article article = new Article();
            articleAddBodyDTO.convertTo(article);
            String articleId = UUID.randomUUID().toString().substring(0, 32);
            article.setId(articleId);
            article.setUserId(userId);
            article.setStatusId(ArticleStatusEnums.DRAFT.getValue());
            int isArticleSave = articleMapper.insert(article);

            // 保存ArticleTag数据
            if (!StringUtils.isEmpty(articleAddBodyDTO.getTagIds())) {
                for (String tag : articleAddBodyDTO.getTagIds().split(SPLITOR)) {
                    ArticleTag articleTag = new ArticleTag();
                    articleTag.setId(articleId);
                    articleTag.setTagId(Integer.valueOf(tag));
                    articleTagMapper.insert(articleTag);
                }
            }

            if (isArticleSave == 1) {
                return Result.success("文章保存成功", true);
            } else {
                return Result.fail(500, "文章保存失败", MsgType.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            // 显式回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail(403, "文章保存失败", MsgType.ERROR_MESSAGE);
        }
    }

    /**
     * @param articleListParams ListParamsVO<ArticleListParamsVO, ArticleListSortVO, ArticleListFilterVO>
     * @return Result
     * @description 管理员分页查询文章
     */
    @Override
    public Result adminGetList(ListParamsVO<ArticleListParamsVO, ArticleListSortVO, ArticleListFilterVO> articleListParams) {
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
        List<ArticleInfoResWithAdminDTO> articleInfoResDTO = null;
        // 总条数
        int total = 0;
        try {
            articleInfoResDTO = articleMapper.listArticlesWithLimit(params, sort, filter, listPageVO);
            total = articleMapper.listArticlesWithNotLimit(params, sort, filter, listPageVO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 封装数据
        ListResDTO<ArticleInfoResWithAdminDTO> articleListRes = new ListResDTO<>();
        articleListRes.setCurrent(page.getCurrent());
        articleListRes.setPageSize(page.getPageSize());
        articleListRes.setList(articleInfoResDTO);
        articleListRes.setTotal(total);

        return Result.success("获取成功", articleListRes);
    }

    /**
     * @param uArticleListParams UArticleListParamsDTO
     * @return Result
     * @description 管理员分页查询文章
     */
    @Override
    public Result userGetList(UArticleListParamsDTO uArticleListParams, HttpServletRequest request) {
        // 1. 判断合法性
        // 1.1 获取token
        String token = request.getHeader(USER_TOKEN_KEY_IN_HEADER);
        Claims userInfoInToken = null;
        try {
            userInfoInToken = Token.parse(token, USER_TOKEN_SECRET);
        } catch (Exception e) {
            log.info("错误令牌");
        }
        // 查询数据
        List<ArticleInfoResWithUserDTO> articleInfoResWithUserDTOS = null;
        int total = 0;
        try {
            articleInfoResWithUserDTOS = articleMapper.listArticlesWithLimitAndUser(
                    uArticleListParams.getTagId(),
                    uArticleListParams.getCategoryId(),
                    uArticleListParams.getCurrent(),
                    uArticleListParams.getPageSize(),
                    !ObjectUtil.isEmpty(userInfoInToken) ? Objects.requireNonNull(userInfoInToken).get("id").toString() : ""
            );
            total = articleMapper.listArticlesWithNotLimitAndUser(
                    uArticleListParams.getTagId(),
                    uArticleListParams.getCategoryId()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 封装数据
        ListResDTO<ArticleInfoResWithUserDTO> articleListRes = new ListResDTO<>();
        if (articleInfoResWithUserDTOS != null) {
            articleListRes.setCurrent(uArticleListParams.getCurrent());
            articleListRes.setPageSize(uArticleListParams.getPageSize());
            articleListRes.setList(articleInfoResWithUserDTOS);
            articleListRes.setTotal(total);
        }

        return Result.success("获取成功", articleListRes);
    }

    /**
     * @param uArticleListParamsWithUserIdDTO UArticleListParamsWithUserIdDTO
     * @param request                         HttpServletRequest
     * @return Result
     * @description 管理员分页查询文章
     */
    @Override
    public Result listWithUserId(UArticleListParamsWithUserIdDTO uArticleListParamsWithUserIdDTO, HttpServletRequest request) {
        // 查询数据
        List<ArticleInfoResWithUserDTO> articleInfoResWithUserDTOS = null;
        int total = 0;
        try {
            articleInfoResWithUserDTOS = articleMapper.listArticlesWithLimitAndUserId(
                    uArticleListParamsWithUserIdDTO.getStatusId(),
                    uArticleListParamsWithUserIdDTO.getCurrent(),
                    uArticleListParamsWithUserIdDTO.getPageSize(),
                    uArticleListParamsWithUserIdDTO.getUserId()
            );
            total = articleMapper.listArticlesWithNotLimitAndUserId(
                    uArticleListParamsWithUserIdDTO.getStatusId(),
                    uArticleListParamsWithUserIdDTO.getUserId()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 封装数据
        ListResDTO<ArticleInfoResWithUserDTO> articleListRes = new ListResDTO<>();
        if (articleInfoResWithUserDTOS != null) {
            articleListRes.setCurrent(uArticleListParamsWithUserIdDTO.getCurrent());
            articleListRes.setPageSize(uArticleListParamsWithUserIdDTO.getPageSize());
            articleListRes.setList(articleInfoResWithUserDTOS);
            articleListRes.setTotal(total);
        }

        return Result.success("获取成功", articleListRes);
    }

    /**
     * @param articleId String
     * @param request   HttpServletRequest
     * @return Result
     * @description 用户获取文章详情
     */
    @Override
    public Result getArticleAndUserInfoByIdWithUser(String articleId, HttpServletRequest request) throws AccessException {
        ArticleDetailInfoResWithUserDTO articleDetailInfoResWithUserDTO = new ArticleDetailInfoResWithUserDTO();
        String token = request.getHeader(USER_TOKEN_KEY_IN_HEADER);

        // 1. 获取文章信息
        ArticleDetailInfoVO articleDetailInfo;
        if (StringUtils.isEmpty(token)) {
            articleDetailInfo = articleMapper.selectArticleDetailInfo(articleId, "");
            articleDetailInfo.setIsLiked(false);
            articleDetailInfo.setIsCollected(false);
        } else {
            // 获取token
            Claims userInfoInToken;
            try {
                userInfoInToken = Token.parse(token, USER_TOKEN_SECRET);
            } catch (Exception e) {
                throw new AccessException("token解析失败");
            }
            // 获取用户ID
            String userId = (String) userInfoInToken.get("id");
            articleDetailInfo = articleMapper.selectArticleDetailInfo(articleId, userId);
        }

        // 2. 获取作者信息
        AuthorDetailInfoVO authorDetailInfo = articleMapper.selectUserById(articleId);
        List<Integer> tagIds = userTagMapper.selectTagIdsById(authorDetailInfo.getId());
        authorDetailInfo.setTagIds(tagIds);

        // 3. 获取作者动态信息
        AuthorArticleDetailInfoVO authorArticleDetailInfo = new AuthorArticleDetailInfoVO();
        // 3.1 获取作者最新动态文章
        List<ArticleLinkBO> articleLatest = userArticleMapper.selectSpecificTypeArticlesById(
                authorDetailInfo.getId(),
                UserArticleTypeEnums.PUBLISHED.getValue(),
                0,
                5,
                "createdTime",
                SortedTypeEnums.DESC
        );
        // 3.2 获取作者置顶动态文章
        List<ArticleLinkBO> articleTopest = userArticleMapper.selectSpecificTypeArticlesById(
                authorDetailInfo.getId(),
                UserArticleTypeEnums.PINNED.getValue(),
                0,
                5,
                "createdTime",
                SortedTypeEnums.DESC
        );
        // 3.3 设置
        authorArticleDetailInfo.setLatestArticles(articleLatest);
        authorArticleDetailInfo.setPinnedArticles(articleTopest);

        // 4. 获取前十条评论
        List<CommentDetailInfoVO> commentDetailInfoVOS = articleCommentMapper.selectCommentInfoLimitById(articleId, 0, 10);

        // 5. 数据整合
        articleDetailInfoResWithUserDTO.allSet(articleDetailInfo, authorDetailInfo, authorArticleDetailInfo, commentDetailInfoVOS);
        return Result.success("获取成功", articleDetailInfoResWithUserDTO, MsgType.SILENT);
    }

    /**
     * @param articleId String
     * @param request   HttpServletRequest
     * @return Result
     * @description 点赞获取消点赞
     */
    @Override
    public Result like(String articleId, HttpServletRequest request) {
        return articleOperation(articleId, request, UserArticleTypeEnums.LIKED);
    }

    /**
     * @param articleId String
     * @param request   HttpServletRequest
     * @return Result
     * @description 收藏获取消收藏
     */
    @Override
    public Result collect(String articleId, HttpServletRequest request) {
        return articleOperation(articleId, request, UserArticleTypeEnums.COLLECTED);
    }

    /**
     * @param articleId String
     * @param request   HttpServletRequest
     * @return Result
     * @description 浏览过
     */
    @Override
    public Result visit(String articleId, HttpServletRequest request) {
        // 1. 判断合法性
        // 1.1 获取token
        String token = request.getHeader(USER_TOKEN_KEY_IN_HEADER);
        if (StringUtils.isEmpty(token)) {
            return Result.fail(403, "非法请求", MsgType.WARN_MESSAGE);
        }
        // 1.2 获取文章是否存在
        if (StringUtils.isEmpty(articleId)) {
            return Result.fail(403, "参数错误", MsgType.WARN_MESSAGE);
        }
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            return Result.fail(403, "文章不存在", MsgType.WARN_MESSAGE);
        }
        // 1.3 获取该文章是否已审核
        if (article.getStatusId() != ArticleStatusEnums.AUDITED.getValue()) {
            return Result.fail(403, "非法操作", MsgType.WARN_MESSAGE);
        }
        try {
            Claims userInfoInToken = Token.parse(token, USER_TOKEN_SECRET);
            // 1.2 获取该用户是否已操作
            QueryWrapper<UserArticle> userArticleOprationQueryWrapper = new QueryWrapper<>();
            userArticleOprationQueryWrapper
                    .eq("id", userInfoInToken.get("id"))
                    .eq("articleId", articleId)
                    .eq("type", UserArticleTypeEnums.VISITED.getValue());
            boolean isOperated = userArticleMapper.exists(userArticleOprationQueryWrapper);
            if (!isOperated) {
                UserArticle userArticle = new UserArticle();
                userArticle.setId((String) userInfoInToken.get("id"));
                userArticle.setArticleId(articleId);
                userArticle.setType(UserArticleTypeEnums.VISITED.getValue());
                int isInsert = userArticleMapper.insert(userArticle);
                if (isInsert == 1) {
                    return Result.success(UserArticleTypeEnums.VISITED.getName() + "成功", true, MsgType.SILENT);
                } else {
                    return Result.fail(400, UserArticleTypeEnums.VISITED.getName() + "失败", false, MsgType.SILENT);
                }
            }
            return Result.success(UserArticleTypeEnums.VISITED.getName() + "成功", true, MsgType.SILENT);
        } catch (Exception e) {
            return Result.fail(403, "参数错误", MsgType.WARN_MESSAGE);
        }
    }

    /**
     * @param userId   String
     * @param current  int
     * @param pageSize int
     * @return Result
     * @description 获取浏览过的文章
     */
    @Override
    public Result listVisited(String userId, int current, int pageSize) {
        if (StringUtils.isEmpty(userId)) {
            return Result.fail(403, "参数错误", MsgType.WARN_MESSAGE);
        }
        if (pageSize >= MAX_COUNT_PER_LIST) {
            return Result.fail(403, "非法请求", MsgType.ERROR_MESSAGE);
        }
        List<ArticleLinkBO> articleLinkBOS = userArticleMapper.selectSpecificTypeArticlesById(
                userId,
                UserArticleTypeEnums.VISITED.getValue(),
                pageSize * (current - 1),
                pageSize,
                "createdTime",
                SortedTypeEnums.DESC
        );
        return Result.success("获取成功", articleLinkBOS, MsgType.SILENT);
    }

    /**
     * @param articleId String
     * @param request   HttpServletRequest
     * @return Result
     * @description 操作封装方法
     */
    public Result articleOperation(String articleId, HttpServletRequest request, UserArticleTypeEnums userArticleTypeEnums) {
        // 1. 判断合法性
        // 1.1 获取token
        String token = request.getHeader(USER_TOKEN_KEY_IN_HEADER);
        if (StringUtils.isEmpty(token)) {
            return Result.fail(403, "非法请求", MsgType.WARN_MESSAGE);
        }
        // 1.2 获取文章是否存在
        if (StringUtils.isEmpty(articleId)) {
            return Result.fail(403, "参数错误", MsgType.WARN_MESSAGE);
        }
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            return Result.fail(403, "文章不存在", MsgType.WARN_MESSAGE);
        }
        // 1.3 获取该文章是否已审核
        if (article.getStatusId() != ArticleStatusEnums.AUDITED.getValue()) {
            return Result.fail(403, "非法操作", MsgType.WARN_MESSAGE);
        }
        try {
            Claims userInfoInToken = Token.parse(token, USER_TOKEN_SECRET);
            // 1.2 获取该用户是否已操作
            QueryWrapper<UserArticle> userArticleOprationQueryWrapper = new QueryWrapper<>();
            userArticleOprationQueryWrapper
                    .eq("id", userInfoInToken.get("id"))
                    .eq("articleId", articleId)
                    .eq("type", userArticleTypeEnums.getValue());
            boolean isOperated = userArticleMapper.exists(userArticleOprationQueryWrapper);
            if (isOperated) {
                int isDelete = userArticleMapper.delete(userArticleOprationQueryWrapper);
                if (isDelete == 1) {
                    return Result.success("取消" + userArticleTypeEnums.getName() + "成功", false, MsgType.NOTIFICATION);
                } else {
                    return Result.fail(400, "取消" + userArticleTypeEnums.getName() + "失败", true, MsgType.ERROR_MESSAGE);
                }
            } else {
                UserArticle userArticle = new UserArticle();
                userArticle.setId((String) userInfoInToken.get("id"));
                userArticle.setArticleId(articleId);
                userArticle.setType(userArticleTypeEnums.getValue());
                int isInsert = userArticleMapper.insert(userArticle);
                if (isInsert == 1) {
                    return Result.success(userArticleTypeEnums.getName() + "成功", true, MsgType.NOTIFICATION);
                } else {
                    return Result.fail(400, userArticleTypeEnums.getName() + "失败", false, MsgType.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            return Result.fail(403, "参数错误", MsgType.WARN_MESSAGE);
        }
    }
}
