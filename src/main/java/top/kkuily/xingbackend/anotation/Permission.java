package top.kkuily.xingbackend.anotation;

import top.kkuily.xingbackend.model.enums.AuthEnums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 小K
 * @description 管理员权限校验
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Permission {
    AuthEnums authId();
}