package top.kkuily.xingbackend.model.enums;

/**
 * @author 小K
 * @description 用户与文章之间的关系
 */
public enum UserArticleTypeEnums {
    /**
     * 喜欢
     */
    LIKED("喜欢", 1),
    /**
     * 发布
     */
    PUBLISHED("发布", 2),
    /**
     * 收藏
     */
    COLLECTED("收藏", 3),
    /**
     * 置顶
     */
    PINNED("置顶", 4),
    /**
     * 浏览过
     */
    VISITED("浏览过", 5);

    private final String name;
    private final int value;

    /**
     * 构造函数，用于为每个枚举常量分配一个整数值。
     *
     * @param value 整数值
     */
    UserArticleTypeEnums(String name, int value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 获取枚举常量的值。
     *
     * @return 枚举常量的值
     */
    public int getValue() {
        return value;
    }

    /**
     * 获取枚举常量的值。
     *
     * @return 枚举常量的名称
     */
    public String getName() {
        return name;
    }
}
