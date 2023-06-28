package top.kkuily.xingbackend.model.enums;

/**
 * @author 小K
 * @description 排序枚举
 */

public enum SortedTypeEnums {
    /**
     * 升序
     */
    ASC("ASC"),
    /**
     * 降序
     */
    DESC("ASC");

    private final String value;

    /**
     * 构造函数，用于为每个枚举常量分配一个整数值。
     *
     * @param value 整数值
     */
    SortedTypeEnums(String value) {
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
