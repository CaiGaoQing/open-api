package com.sharecharger.open.util;

/**
 * @Author 蔡高情
 * @Description  获取加密的key
 * @Date 15:44 2018/7/9 0009
 * @Param 
 * @return 
 **/

public interface GetEncryptData {


    /**
     * 根据appid查询AesKey
     * @return
     */
     String getAesKey(String appId);

    /**
     * 根据appid查询AesIv
     * @return
     */
     String getAesIv(String appId);
    /**
     * 根据appid查询appSecret 校验签证
     * @return
     */
     String getAppSecret(String appId);
}
