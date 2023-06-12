package top.kkuily.xingbackend.model.enums;

/**
 * @author 小K
 * @description 文章分类枚举
 */
public enum ArticleCategoryEnums {
    /**
     * 最新文章
     */
    NEWEST(1),

    /**
     * 热门文章
     */
    HOT(2),

    /**
     * 优质文章
     */
    HIGH_QUALITY(3);

    private final int value;

    ArticleCategoryEnums(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
