package top.kkuily.xingbackend.model.enums;

public enum IsDeleted {
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
    IsDeleted(String value) {
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
