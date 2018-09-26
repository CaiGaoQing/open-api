package com.sharecharger.open.common.support;

import com.sharecharger.open.common.annotation.CheckTimePar;
import com.sharecharger.open.common.container.ResponseMsgNametContainer;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author 蔡高情
 * @Description  自定义注解判断 传入时间 -当前时间 大于60秒
 * @Date 10:53 2018/7/12 0012
 * @Param 
 * @return 
 **/

public class CheckTimeParValidator implements ConstraintValidator<CheckTimePar, String> {
    private CheckTimePar checkTimePar;

    public void initialize(CheckTimePar checkTimePar) {
        this.checkTimePar=checkTimePar;
    }

    @Override
    public boolean isValid(String timeStr, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isEmpty(timeStr)){
            return false;
        }
        Long nowTime= System.currentTimeMillis()/1000;
        Long Time=Long.valueOf(timeStr);
        Long differenceTime = ( nowTime- Time);
        //时间60s
        if (differenceTime > ResponseMsgNametContainer.REQUEST_TIME_FLAG_MIN){
           return false;
        }
        return true;
    }

}