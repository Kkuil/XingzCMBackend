package top.kkuily.xingbackend.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

import static top.kkuily.xingbackend.constant.commons.Api.PHONE_REG;

/**
 * 验证工具
 */
public class ValidateUtils {
    /**
     * @param fieldName String
     * @param value     String
     * @description 验证字段是为空
     */
    public static void validateNotEmpty(String fieldName, String value) {
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException(fieldName + "不能为空");
        }
    }

    /**
     * @param value     String
     * @param fieldName String
     * @param minLength int
     * @param maxLength int
     * @description 判断长度是偶符合要求
     */
    public static void validateLength(String fieldName, String value, int minLength, int maxLength) {
        if (StringUtils.length(value) < minLength || StringUtils.length(value) > maxLength) {
            throw new IllegalArgumentException(fieldName + "长度必须在" + minLength + "到" + maxLength + "之间");
        }
    }

    /**
     * @param value     String
     * @param fieldName String
     * @description 验证手机号的合法性
     */
    public static void validateMobile(String fieldName, String value) {
        ValidateUtils.validateNotEmpty(fieldName, value);
        if (!Pattern.matches(PHONE_REG, value)) {
            throw new IllegalArgumentException(fieldName + "格式不正确");
        }
    }
}
