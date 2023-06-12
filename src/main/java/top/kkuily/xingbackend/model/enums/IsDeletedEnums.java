package top.kkuily.xingbackend.model.enums;

/**
 * @author 小K
 */

public enum IsDeletedEnums {
    /**
     * 未删除
     */
    NO("0"),
    /**
     *
     */
    YES("1");

    private final String value;

    /**
     * 构造函数，用于为每个枚举常量分配一个整数值。
     *
     * @param value 整数值
     */
    IsDeletedEnums(String value) {
        this.value = value;
    }

    /**
     * 获取枚举常量的值。
     *
     * @return 枚举常量的值
     */
    public String getValue() {
        return value;
    }
}
