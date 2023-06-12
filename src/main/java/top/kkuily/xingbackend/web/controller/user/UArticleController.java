package top.kkuily.xingbackend.web.controller.user;

import cn.hutool.core.lang.UUID;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import top.kkuily.xingbackend.anotation.UserAuthToken;
import top.kkuily.xingbackend.constant.commons.MsgType;
import top.kkuily.xingbackend.model.dto.request.article.ArticleAddBodyDTO;
import top.kkuily.xingbackend.model.dto.request.article.ArticleUpdateBodyDTO;
import top.kkuily.xingbackend.model.po.Article;
import top.kkuily.xingbackend.model.po.ArticleStatistic;
import top.kkuily.xingbackend.model.po.ArticleStatus;
import top.kkuily.xingbackend.service.IArticleService;
import top.kkuily.xingbackend.service.IArticleStatisticService;
import top.kkuily.xingbackend.service.IArticleStatusService;
import top.kkuily.xingbackend.utils.*;

import java.util.Arrays;

import static top.kkuily.xingbackend.constant.aritcle.ArticleInfo.*;
import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_KEY_IN_HEADER;
import static top.kkuily.xingbackend.constant.user.Auth.USER_TOKEN_SECRET;

/**
 * @author 小K
 * @description 文章相关接口
 */
@RestController
@Slf4j
public class UArticleController {

    @Resource
    private IArticleService articleService;

    @Resource
    private IArticleStatusService statusService;


    @Resource
    private IArticleStatisticService articleStatisticService;

    // region
    // 增删改查

    /**
     * @param articleAddBodyDTO AuthAddBodyDTO
     * @return Result
     * @description 发布
     */
    @PostMapping("uarticle")
    @UserAuthToken
    @Transactional(rollbackFor = Exception.class)
    public Result publish(@RequestBody ArticleAddBodyDTO articleAddBodyDTO, HttpServletRequest request) {
        try {
            try {
                // 判空
                ValidateUtils.validateNotEmpty("标题", articleAddBodyDTO.getTitle());
                ValidateUtils.validateNotEmpty("内容", articleAddBodyDTO.getContent());
                // 判长
                ValidateUtils.validateLength("标题", articleAddBodyDTO.getTitle(), ARTICLE_TITLE_LEN_RANGE[0], ARTICLE_TITLE_LEN_RANGE[1]);
                ValidateUtils.validateLength("内容", articleAddBodyDTO.getContent(), ARTICLE_CONTENT_LEN_RANGE[0], ARTICLE_CONTENT_LEN_RANGE[1]);
                if (articleAddBodyDTO.getTagIds() != null && articleAddBodyDTO.getTagIds().length > ARTICLE_MAX_TAG_COUNT) {
                    throw new IllegalArgumentException("标签数量过多，请选择合适数量的标签");
                }
                // 查询statusId是否存在
                Integer statusId = articleAddBodyDTO.getStatusId();
                if (statusId != null) {
                    ArticleStatus articleStatus = statusService.getById(statusId);
                    if (articleStatus == null) {
                        throw new IllegalArgumentException("状态不存在，非法操作");
                    }
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
            boolean isArticleSave = articleService.save(article);

            // 保存ArticleStatistic数据
            ArticleStatistic articleStatistic = new ArticleStatistic();
            articleStatistic.setId(articleId);
            if(articleAddBodyDTO.getTagIds() != null) {
                articleStatistic.setTagIds(Arrays.toString(articleAddBodyDTO.getTagIds()));
            }
            boolean isArticleStatisticSave = articleStatisticService.save(articleStatistic);

            if (isArticleSave && isArticleStatisticSave) {
                return Result.success("文章发布成功", true);
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
     * @param id String
     * @return Result
     * @description 删
     */
    @DeleteMapping("uarticle")
    public Result del(String id) {
        return null;
    }

    /**
     * @param articleUpdateBodyDTO AuthUpdateBodyDTO
     * @return Result
     * @description 改
     */
    @PutMapping("uarticle")
    public Result update(@RequestBody ArticleUpdateBodyDTO articleUpdateBodyDTO) {
        return Result.success("更新成功", true);
    }

    /**
     * @param id String
     * @return Result
     * @description 获取某个文章
     */
    @GetMapping("/uarticle/:id")
    public Result get(@PathParam("id") String id) {
        return null;
    }
    // endregion
}
