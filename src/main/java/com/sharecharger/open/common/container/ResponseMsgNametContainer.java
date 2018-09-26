package com.sharecharger.open.common.container;

/**
 * @author 蔡高情
 * @Title: ResponseMsgNametContainer
 * @ProjectName spring-custom
 * @Description:
 * @date 2018/7/9 000915:04
 */
public interface ResponseMsgNametContainer {
    //签名验证错误 code //认证失败消息
    int ERROE_RESPONSE_SIGNATURE_CODE=4001;
     String ERROE_RESPONSE_SIGNATURE_MTRINGSG="签名认证失败！";

    //认证参数不合法，不如缺少appId、timestamp等
    int ERROE_RESPONSE_PAR_CODE=4002;
    String ERROE_RESPONSE_PAR_MSG="请求参数不合法！";

    //通用错误
    int ERROE_RESPONSE_CODE=1;
    String ERROE_RESPONSE_MSG="未知异常！";

    //请求成功
    String SUCCESS_RESPONSE_MSG="ok";
    int SUCCESS_RESPONSE_CODE=0;

    //时间戳大于多少秒 认证失败！
    int REQUEST_TIME_FLAG_MIN=60;
    String ERROE_REQUEST_TIME_FLAG_MSG="请求数据时长大于60S!";


    String ERROE_RESPONSE_RES_MSG="未获取到APP对应的加密KEY!";

    String ERROE_GETBODY_MSG="解密失败！";

    String ERROE_NULL_TIME_MSG="时间戳不能为空！";

    String ERROE_NULL_DATA_MSG="返回数据为空!";

}
