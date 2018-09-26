package com.sharecharger.open.common.support;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sharecharger.open.common.annotation.RequestBodyDecrypt;
import com.sharecharger.open.common.container.RequesNametContainer;
import com.sharecharger.open.common.container.SystemConfigContainer;
import com.sharecharger.open.common.entity.JSONObjectWrapper;
import com.sharecharger.open.common.entity.MapWrapper;
import com.sharecharger.open.common.entity.RequestData;
import com.sharecharger.open.common.exception.SharechargerOpenException;
import com.sharecharger.open.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 蔡高情
 * @Description  自定义HandlerMethodArgumentResolver解析
 * @Date 17:39 2018/7/16 0016
 * @Param 
 * @return 
 **/
@Slf4j
public class RequestBodyDecryptArgumentResolver implements HandlerMethodArgumentResolver {

    private GetEncryptData getEncryptData;
    //构造注入 获取加密的key的接口
    public  RequestBodyDecryptArgumentResolver(GetEncryptData getEncryptData) {
        this.getEncryptData = getEncryptData;
    }

    //判断是否支持要转换的参数类型
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBodyDecrypt.class);
    }


    //当支持后进行相应的转换
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception{
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
       //boolean isRequired =parameter.getParameterAnnotation(RequestBodyDecrypt.class).required();
        String openRequestBodyDecryptStr=System.getProperty(SystemConfigContainer.OPEN_REQUESTBODY_DECRYPT);
        boolean isRequired=true;
        if (StringUtils.isNotEmpty(openRequestBodyDecryptStr)){
            boolean status=Boolean.valueOf(openRequestBodyDecryptStr);
            //定义的 不能转换成功为false并且openRequestBodyDecryptStr是false才是
            if(!status && openRequestBodyDecryptStr.equals("false")){
                isRequired=false;
            }
        }


        //获取全部的所有参数
        Map<String, String[]> parMap=request.getParameterMap();
        //获取加密的postBody
        String  body= GetRequestDataUtil.getEncryBody(request);
        if (isRequired){
            JSONObject obj= JSON.parseObject(body);
            //获取加密的body
            String encryptBody=obj.getString(RequesNametContainer.SCOP_BODY_POSTBODY_NAME);
            //获取appid
            String appIdPar=GetRequestDataUtil.getAppId(request);
            //获取客户端传递的签名
            String signaturePar = GetRequestDataUtil.getSignature(request);
            //获取请求的时间戳
            String timestampPar=GetRequestDataUtil.getTimestamp(request);
           //获取APPID的加密签证的key
            String appSecret = getEncryptData.getAppSecret(appIdPar);
            //获取解析postBody加密的key 用于解密postBody
            String aesIv = getEncryptData.getAesIv(appIdPar);
            //获取解析postBody加密的偏移量  用于解密postBody
            String aesKey = getEncryptData.getAesKey(appIdPar);
            String[] appIdParArr = new String[]{appIdPar};
            String[] aesIvArr = new String[]{aesIv};
            String[] aesKeyParArr = new String[]{aesKey};
            Map<String,String[]> map = new HashMap<String,String[]>();
            map.putAll(parMap);
            map.put("app_id",appIdParArr);
            map.put("aes_iv",aesIvArr);
            map.put("aes_key",aesKeyParArr);
            RequestData requestData=new RequestData();
            requestData.setEncryBody(body);
            requestData.setAppId(appIdPar);
            requestData.setSignaturePar(signaturePar);
            requestData.setTimestampPar(timestampPar);
            requestData.setAppSecret(appSecret);
            requestData.setAesKey(aesKey);
            requestData.setAesIv(aesIv);
            //校验参数
            ValidatorUtils.validateEntity(requestData);
            //获取postBody
            body=this.getRequestBody(map,signaturePar,appSecret,aesKey,aesIv,encryptBody);
        }
         return resolve(parameter,body);
    }


    private Object resolve (MethodParameter parameter,String bodyObj){
        Object result;

        log.debug("GET HTTP RequestBodyData IS " +bodyObj);
        Class clazz = parameter.getParameterType();
        if (StringUtils.isEmpty(bodyObj)) {
            return null;
        }
        if (clazz.equals(List.class)){
            result= JSON.parseArray(bodyObj);
        }
        // 利用fastjson转换为对应的类型
        if(JSONObjectWrapper.class.isAssignableFrom(parameter.getParameterType())){
            result= new JSONObjectWrapper(JSON.parseObject(bodyObj));
        }

        result= JSON.parseObject(bodyObj.toString(), parameter.getParameterType());
        //如果有key则获取key的值返回
        String key=parameter.getParameterAnnotation(RequestBodyDecrypt.class).value();
        if (StringUtils.isNotEmpty(key)){
            JSONObject data= JSON.parseObject(bodyObj.toString());
            //获取json的key
            result=data.get(key);

        }
        //mapWrapper类型
        if (MapWrapper.class.isAssignableFrom(clazz)){
            MapWrapper mapWrapper=new MapWrapper();
            Map target =(Map) JSON.parseObject(bodyObj.toString());
            mapWrapper.setInnerMap(target);
            result=(Object)mapWrapper;
        }



        return result;
    }

    /**
     * 获取提交的post json
     * 验证加密
     * @param
     * @return
     */
    private String getRequestBody(Map<String,String[]> parMap,String signaturePar,String appSecret,String aesKey,String aesIv,String encryBody) throws SharechargerOpenException {


        //获得签名
        String signature=SecurityUtils.getSignature(appSecret,parMap,encryBody);

        //校验  失败直接异常处理
        RequestDataValidatorUtil.validatorSignature(signature,signaturePar);

        //解析完后 的传递参数
        String postBody = SecurityUtils.getDecryptBody(aesKey,aesIv,encryBody);

        return postBody;
    }

}