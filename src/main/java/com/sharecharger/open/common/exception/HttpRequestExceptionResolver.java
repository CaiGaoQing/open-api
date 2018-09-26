package com.sharecharger.open.common.exception;

import com.alibaba.fastjson.JSON;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;

/**
 * @Author 蔡高情
 * @Description  自定义异常处理
 * @Date 14:07 2018/7/9 0009
 * @Param 
 * @return 
 **/
public class HttpRequestExceptionResolver implements HandlerExceptionResolver {

    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse response, Object o, Exception ex) {
        SharechargerOpenException customException = null;
        //如果抛出的是系统自定义异常则直接转换
        if (ex instanceof SharechargerOpenException) {
            customException = (SharechargerOpenException) ex;
        } else {
            //打印系统异常
            ex.printStackTrace();
        }
        response.setCharacterEncoding("utf-8");

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/json; charset=utf-8");
            String responseJSONObject = JSON.toJSONString(customException.getResult());
            out.write(responseJSONObject.toString().getBytes("UTF-8"));
            out.flush();
        } catch (HTTPException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception p) {
                    p.printStackTrace();
                }
            }
            return null;
        }
    }

}