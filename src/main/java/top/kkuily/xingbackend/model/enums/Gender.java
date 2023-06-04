package top.kkuily.xingbackend.model.enums;

/**
 * @author 小K
 * @description 枚举类型，用于封装性别信息。
 */
public enum Gender {
    /**
     * 未知性别，对应数据库中的值为2。
     */
    UNKNOWN(2),
    /**
     * 女性，对应数据库中的值为0。
     */
    FEMALE(0),
    /**
     * 男性，对应数据库中的值为1。
     */
    MALE(1);

    private final int value;

    /**
     * 构造函数，用于为每个枚举常量分配一个整数值。
     *
     * @param value 整数值
     */
    Gender(int value) {
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
}


