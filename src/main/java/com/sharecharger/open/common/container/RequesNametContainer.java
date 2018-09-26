package com.sharecharger.open.common.container;

/**
 * @author 蔡高情
 * @Title: HeadContainer  head常量
 * @ProjectName spring-custom
 * @Description:
 * @date 2018/7/9 000912:44
 */
public interface RequesNametContainer {
    //appId
   String SCOP_HEAD_APPID_NAME="app_id";
    //加密签名
    String SCOP_HEAD_SIGNATURE_NAME="signature";

    //请求的时间戳
    String SCOP_GET_TIMESTAMP_NAME="timestamp";

    //随机字符串
    String SCOP_GET_NONCE_NAME="nonce";

    String SCOP_BODY_POSTBODY_NAME="postbody";

    String JSON_REQUEST_BODY = "JSON_REQUEST_BODY";


}
