package top.kkuily.xingbackend.anotation;

import top.kkuily.xingbackend.model.enums.AUTHEnums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 小K
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Permission {
    AUTHEnums authId();
}