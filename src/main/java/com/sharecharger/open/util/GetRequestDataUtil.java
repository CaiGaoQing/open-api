package com.sharecharger.open.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sharecharger.open.common.container.RequesNametContainer;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author 蔡高情
 * @Title: HeadUtil
 * @ProjectName spring-custom
 * @Description: x
 * @date 2018/7/9 000912:39
 */
public class GetRequestDataUtil {


    /**
     * @Author 蔡高情
     * @Description //x 获取客户端上传的签名
     * @Date 12:53 2018/7/9 0009
     * @Param [request]
     * @return java.lang.String
     **/
    public static String getEncryBody( HttpServletRequest request){
         String jsonBody = (String) request.getAttribute(RequesNametContainer.JSON_REQUEST_BODY);
        if (jsonBody == null) {
            try {
                jsonBody = IOUtils.toString(request.getInputStream());
                request.setAttribute(RequesNametContainer.JSON_REQUEST_BODY,jsonBody);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonBody;
    }

    /**
     * @Author 蔡高情
     * @Description 获取head 中的appId
     * @Date 12:40 2018/7/9 0009
     * @Param []
     * @return java.lang.String
     **/
    public static String getAppId( HttpServletRequest request){
        return request.getHeader(RequesNametContainer.SCOP_HEAD_APPID_NAME);
    }

    /**
     * @Author 蔡高情
     * @Description //x 获取客户端head上传的签名
     * @Date 12:53 2018/7/9 0009
     * @Param [request]
     * @return java.lang.String
     **/
    public static String getSignature( HttpServletRequest request){
        return request.getHeader(RequesNametContainer.SCOP_HEAD_SIGNATURE_NAME);
    }

    /**
     * @Author 蔡高情
     * @Description //x 获取客户端上传的时间戳
     * @Date 12:53 2018/7/9 0009
     * @Param [request]
     * @return java.lang.String
     **/
    public static String getTimestamp( HttpServletRequest request){
        return request.getParameter(RequesNametContainer.SCOP_GET_TIMESTAMP_NAME);
    }

}
