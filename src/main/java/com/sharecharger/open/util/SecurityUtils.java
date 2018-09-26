/**
 * Copyright (C), 2018-2018,
 * FileName: SecurityUtils
 * Author:   $蔡高情
 * Date:     2018/7/5 20:06
 * Description: 测试
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名  蔡高情       描述
 */
package com.sharecharger.open.util;

import com.sharecharger.open.common.container.ResponseMsgNametContainer;
import com.sharecharger.open.common.entity.R;
import com.sharecharger.open.common.exception.SharechargerOpenException;
import com.sharecharger.open.util.encrypt.AesCBC;
import com.sharecharger.open.util.encrypt.Hmac;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;


/**
 * 〈一句话功能简述〉<br>
 * 〈测试〉
 *
 * @author 蔡高情
 * @create 2018/7/5
 * @since 1.0.0
 */
@Slf4j
public class SecurityUtils {


    /**
     * @param appSecret 加密的key
     * @param
     * @param postBody post请求中json
     * return 返回签名
     */
    public static String getSignature(String appSecret, Map<String,String[]> parMap, String postBody) {

        //排序
        String content=getFormatParams(parMap);
        //生成要签名 加密的内容
        if (StringUtils.isNotEmpty(postBody)){
            content=content+postBody;
        }
        
        content = content.replaceAll("[\\s*\t\n\r]", "");
        
        //加密后的结果
        String signature="";
        try {
            //获取签名
            signature= Hmac.encodeHmacMD5(appSecret,content);
        }catch (Exception e){
            e.printStackTrace();
            log.error("加密签名失败！");
        }
        return signature;
        //****************签名生成完毕***********************//
    }

    /**
     * @param appSecret 加密的key
     * @param content 排序后的参数拼接
     * @param postBody post请求中json
     * return 返回签名
     */
    public static String getSignature(String appSecret, String content, String postBody) {
        //String content=getFormatParams(parMap);
        //生成要签名 加密的内容
        content=content+postBody;
        content = content.replaceAll("[\\s*\t\n\r]", "");
        //加密后的结果
        String signature="";
        try {
            //获取签名
            signature= Hmac.encodeHmacMD5(appSecret,content);
        }catch (Exception e){
            e.printStackTrace();
            log.error("加密签名失败！");
        }
        return signature;
        //****************签名生成完毕***********************//
    }

    /**
     *
     * 对post进行ES-128-CBC加密模式
     * @param aesKey  这是在页面设置的aes加密的key，由开发者配置得到
     * @param aesIv   这是加密的向量 如上方，aes加密的向量，由开发者配置得到
     * @param postbody 请求中postbody中的内容，application/json;utf-8 传递字符串
     * @return 加密后的postbody
     */
    public static String getEncryptBody(String aesKey,String aesIv,String postbody){
        String encryptbody="";
        try {
            encryptbody= AesCBC.getInstance().encrypt(postbody,aesKey,aesIv);
        }catch (Exception e){
            e.printStackTrace();
            log.error("加密失败！");
        }
        return encryptbody;
    }

    /**
     *
     * 对post进行ES-128-CBC解密模式
     * @param aesKey  这是在页面设置的aes加密的key，由开发者配置得到
     * @param aesIv   这是加密的向量 如上方，aes加密的向量，由开发者配置得到
     * @param postbody 请求中postbody中的内容，application/json;utf-8 传递字符串
     * @return 加密后的postbody
     */
    public static String getDecryptBody(String aesKey,String aesIv,String postbody)throws SharechargerOpenException {
        String decryptBody;
        try {
            decryptBody= AesCBC.getInstance().decrypt(postbody,aesKey,aesIv);
        }catch (Exception e){
            e.printStackTrace();
            log.error("解密失败！");
            throw new SharechargerOpenException(R.error(ResponseMsgNametContainer.ERROE_RESPONSE_CODE, ResponseMsgNametContainer.ERROE_GETBODY_MSG));
        }
        return decryptBody;
    }


    /**
     * 获得参数格式化字符串
     * 参数名按字典排序，小写在后面
     */
    private static  String getFormatParams(Map<String,String[]> params){
        List<Map.Entry<String, String[]>> infoIds = new ArrayList<Map.Entry<String, String[]>>(params.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String[]>>() {
            public int compare(Map.Entry<String, String[]> arg0, Map.Entry<String, String[]> arg1) {
                return (arg0.getKey()).compareTo(arg1.getKey());
            }
        });
        String ret = "";

        for (Map.Entry<String, String[]> entry : infoIds) {
            String key=entry.getKey();
            if (key.equals("app_secret")){
                continue;
            }
            ret += key;
            ret += "=";
            ret += entry.getValue()[0];
            ret += "&";
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }


}