package top.kkuily.xingbackend.model.enums;

/**
 * @Author 小K
 * @Description 文章评论点赞状态枚举
 * @Date 2021/8/19
 **/
public enum ArticleCommentReactionEnums {
    /**
     * 点赞
     */
    LIKE(1),
    /**
     * 踩
     */
    DISLIKE(0);
    private final int value;

    ArticleCommentReactionEnums(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
