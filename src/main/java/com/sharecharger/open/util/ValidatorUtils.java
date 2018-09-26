package com.sharecharger.open.util;


import com.sharecharger.open.common.container.ResponseMsgNametContainer;
import com.sharecharger.open.common.entity.R;
import com.sharecharger.open.common.exception.SharechargerOpenException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Map;
import java.util.Set;


public class ValidatorUtils {
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     * @param object        待校验对象
     * @param groups        待校验的组
     * @throws SharechargerOpenException  校验不通过，SharechargerOpenException
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws SharechargerOpenException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        validateSharechargerOpenException(constraintViolations);
    }

    public static void validateEntity(Object obj){
        Map<String,StringBuffer> errorMap = null;
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj,Default.class);
        validateSharechargerOpenException(constraintViolations);
    }

    /**
     * @Author 蔡高情
     * @Description //x 返回错误信息
     * @Date 11:25 2018/7/12 0012
     * @Param [constraintViolations]
     * @return void
     **/
    public static void validateSharechargerOpenException(Set<ConstraintViolation<Object>> constraintViolations){
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for(ConstraintViolation<Object> constraint:  constraintViolations){
                msg.append(constraint.getMessage());
                break;
            }
            R r= new R();
            r=r.error(ResponseMsgNametContainer.ERROE_RESPONSE_PAR_CODE,msg.toString());
            throw new SharechargerOpenException(r);
        }
    }
}