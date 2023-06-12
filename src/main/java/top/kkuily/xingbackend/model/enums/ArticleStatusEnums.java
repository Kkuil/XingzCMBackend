package top.kkuily.xingbackend.model.enums;

/**
 * @author 小K
 * @description 文章状态枚举
 */

public enum ArticleStatusEnums {
    /**
     * 未审核
     */
    UNAUDITED(0),
    /**
     * 已审核
     */
    AUDITED(1),
    /**
     * 草稿
     */
    DRAFT(2),
    /**
     * 已驳回
     */
    REJECTED(3),
    /**
     * 已下架
     */
    UNDERCARRIAGE(4);

    private final int value;

    ArticleStatusEnums(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
