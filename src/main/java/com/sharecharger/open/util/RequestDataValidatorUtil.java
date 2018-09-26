package com.sharecharger.open.util;


import com.sharecharger.open.common.container.ResponseMsgNametContainer;
import com.sharecharger.open.common.entity.R;
import com.sharecharger.open.common.exception.SharechargerOpenException;

/**
 * @Author 蔡高情
 * @Description //x request接收参数的验证是否是合法的
 * @Date 10:23 2018/7/10 0010
 * @Param 
 * @return
 **/
public class RequestDataValidatorUtil {

    /**
     * @Author 蔡高情
     * @Description //x 校验根据appid查询出来的 appSecret  aesKey  aesIv是否为空
     * @Date 10:57 2018/7/10 0010
     * @Param []
     * @return boolean
     **/
    public static boolean validatorSignature(String signature,String signaturePar) throws SharechargerOpenException{
        //签名认证错误
        if (!signaturePar.equals(signature)){
            R r=new R();
            r= r.error(ResponseMsgNametContainer.ERROE_RESPONSE_SIGNATURE_CODE,ResponseMsgNametContainer.ERROE_RESPONSE_SIGNATURE_MTRINGSG);
            throw new SharechargerOpenException(r);
        }
        return true;
    }



}
