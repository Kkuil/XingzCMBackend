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

    /**
     * 根据整数值获取对应的枚举常量。
     *
     * @param value 整数值
     * @return 对应的枚举常量，如果不存在则返回null
     */
    public static Gender getByValue(int value) {
        for (Gender gender : Gender.values()) {
            if (gender.getValue() == value) {
                return gender;
            }
        }
        return null;
    }
}


