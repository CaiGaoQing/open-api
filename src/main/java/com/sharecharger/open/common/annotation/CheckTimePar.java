package com.sharecharger.open.common.annotation;

import com.sharecharger.open.common.support.CheckTimeParValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Author 蔡高情
 * @Description // 自定义注解判断 传入时间 -当前时间 大于60秒
 * @Date 10:53 2018/7/12 0012
 * @Param 
 * @return 
 **/
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckTimeParValidator.class)
@Documented
public @interface CheckTimePar {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}