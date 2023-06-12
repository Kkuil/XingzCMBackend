package top.kkuily.xingbackend.constant.aritcle;

import java.util.Map;

/**
 * 文章信息常量
 *
 * @author 小K
 */
public class ArticleInfo {

    /**
     * 文章封面最大图片大小
     * 5 * 1024 * 1024
     */
    public static final Long MAX_COVER_SIZE = 5242880L;

    /**
     * 文章封面在阿里云OSS上的存储路径
     */
    public static final String ARTICLE_COVER_IN_ALIYUN_OSS_PATH = "resources/article-covers/";

    /**
     * 文章标题长度范围
     */
    public static final Integer[] ARTICLE_TITLE_LEN_RANGE = {5, 20};

    /**
     * 文章内容长度范围
     */
    public static final Integer[] ARTICLE_CONTENT_LEN_RANGE = {30, 500};

    /**
     * 文章最大标签数量
     */
    public static final Integer ARTICLE_MAX_TAG_COUNT = 5;
}
