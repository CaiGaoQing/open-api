package com.sharecharger.open.common.entity;

import com.sharecharger.open.common.annotation.CheckTimePar;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @Author 蔡高情
 * @Description  接收 heand 以及parm传递的参数
 * @Date 10:24 2018/7/12 0012
 * @Param 
 * @return 
 **/

@Data
public class RequestData {

	//加密的postbody数据
	public String encryBody;

	//上传的appId 字符串（16位）
	@NotNull(message = "appId不能为空!")
	@Length(min=16,max = 60 ,message = "appId长度不正确!")
	public String appId;

	//上传的签名
	@NotNull(message = "签名不能为空!")
	public String signaturePar;

	//时间戳
	@NotNull(message = "时间戳不能为空!")
	@CheckTimePar(message = "超过1分钟不处理！")
	public String timestampPar;

	//获取APPID的加密签证的key
	@NotNull(message = "开发者应用secret不能为空!")
	@Length(min=32,max = 32 ,message = "appSecret长度不正确!")
	public String appSecret;

	//获取解析postBody加密的key 用于解密postBody
	@NotNull(message = "开发者应用aesKey不能为空!")
	public String aesKey;

	//获取解析postBody加密的偏移量  用于解密postBody
	@NotNull(message = "aesIv不能为空!")
	public String aesIv;

}
