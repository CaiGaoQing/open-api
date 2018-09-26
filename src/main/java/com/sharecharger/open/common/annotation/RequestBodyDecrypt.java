package com.sharecharger.open.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Author 蔡高情
 * @Description //接收数据 自定义注解解密
 * @Date 11:43 2018/7/9 0009
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestBodyDecrypt {

    String value() default "";

}