package com.sharecharger.open.common.support;

import com.sharecharger.open.common.annotation.ResponseBodyEncrypt;
import com.sharecharger.open.common.container.RequesNametContainer;
import com.sharecharger.open.common.container.SystemConfigContainer;
import com.sharecharger.open.common.entity.R;
import com.sharecharger.open.util.GetEncryptData;
import com.sharecharger.open.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

import java.lang.reflect.Method;


/**
 * @Author 蔡高情
 * @Description  加密响应谁
 * @Date 12:34 2018/7/9 0009
 * @Param 
 * @return 
 **/
@Slf4j
public class ResponseBodyEncryptReturnValueHandler implements HandlerMethodReturnValueHandler {

    private final HandlerMethodReturnValueHandler delegate;

    private GetEncryptData getEncryptData;

    //通过构造注入
    public ResponseBodyEncryptReturnValueHandler(HandlerMethodReturnValueHandler delegate, GetEncryptData getEncryptData) {
        this.delegate = delegate;
        this.getEncryptData=getEncryptData;
    }

    /**
     * 判断方法上的注解是不是ResponseBodyEncrypt
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        //获取类上的注解
        Method method=methodParameter.getMethod();
        Annotation[] classAnnotation =method.getDeclaringClass().getAnnotations();
        boolean flag=false;
        for (int i = 0; i < classAnnotation.length; i++) {
            if(classAnnotation[i].annotationType().equals(RestController.class)){
                flag=true;
                break;
            }
        }
        return methodParameter.getMethodAnnotation
                (ResponseBodyEncrypt.class) != null || methodParameter.getMethodAnnotation(ResponseBody.class) != null ||flag ;
    }

    @Override
    public void handleReturnValue(Object returnValue,
                                  MethodParameter returnType,
                                  ModelAndViewContainer mavContainer,
                                      NativeWebRequest webRequest) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        Class<?> returnParaType = returnType.getParameterType();
        //获取传递的 -d参数 判断是否开关
        String openResponseBodyEncryptStr=System.getProperty(SystemConfigContainer.OPEN_RESPONSEBODY_ENCRYPT);
        boolean defuleType=true;
        boolean status=Boolean.valueOf(openResponseBodyEncryptStr);
        //定义的 不能转换成功为false并且openRequestBodyDecryptStr是false才是
        if (StringUtils.isNotEmpty(openResponseBodyEncryptStr)){
            if(!status && openResponseBodyEncryptStr.equals("false")){
                defuleType=false;
            }
        }
        if (!void.class.isAssignableFrom(returnParaType)) {
            // 不是Response、Model等类型的返回值，需要包裹为Response类型
            if (defuleType && ! HttpServletResponse.class.isAssignableFrom(returnParaType) &&!Model.class.isAssignableFrom(returnParaType) && returnType.getMethodAnnotation(ResponseBodyEncrypt.class) != null) {
                String appId=request.getHeader(RequesNametContainer.SCOP_HEAD_APPID_NAME);
                //获取aesKey aesIv对数据进行加密
                String aesKey= getEncryptData.getAesKey(appId);
                String  aesIv=getEncryptData.getAesIv(appId);
                //返回的数据 没有加密

                //获得返回值类型
                // 不是Response、Model等类型的返回值，需要包裹为Response类型 也要开启 defulttype=true
                boolean flag= R.class.isAssignableFrom(returnParaType);

                String className=returnParaType.getSimpleName();
                boolean classStatus =ignoreCaseEquals(className,"ResponseDataVo");
                if(flag||classStatus){
                    R r=(R)returnValue;
                    Object data=r.get("data");
                    String encryValue= SecurityUtils.getEncryptBody(aesKey,aesIv,data.toString());
                    r.put("data",encryValue);
                    returnValue=r;
                }
                //加密后数据
                String encryValue= SecurityUtils.getEncryptBody(aesKey,aesIv,returnValue.toString());
                returnValue=new R(encryValue);
            }
        }
        delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }

    /**
     *
     *<b>Summary: 忽略大小写比较两个字符串</b>
     * ignoreCaseEquals()
     * @param str1
     * @param str2
     * @return
     */
    public static boolean ignoreCaseEquals(String str1,String str2){
        return str1 == null ? str2 == null :str1.equalsIgnoreCase(str2);
    }

}
