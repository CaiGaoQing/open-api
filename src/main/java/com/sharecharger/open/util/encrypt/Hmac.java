package com.sharecharger.open.util.encrypt;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * @Author 蔡高情
 * @Description  加密工具类
 * @Date 11:50 2018/8/7 0007
 * @Param 
 * @return 
 **/
public class Hmac {

	private static String srcMsg = "";


	/**
	 *	HMAC_MD5 加密算法
	 * @param key  加密的key
	 * @param data 要加密的数据
	 * @return  返回加密值
	 */
	public static String encodeHmacMD5(String key,String data) {
		String encodedMsg="";
		try {
			//初始化KeyGenerator
			KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacMD5");
			//产生密钥
			SecretKey secretKey = keyGenerator.generateKey();
			//获得密钥
//            byte[] key = secretKey.getEncoded();
			byte[] keyb =key.getBytes();
			//还原密钥
			SecretKey restoreSecretKey = new SecretKeySpec(keyb, "HmacMd5");
			//实例化Mac
			Mac mac = Mac.getInstance(restoreSecretKey.getAlgorithm());
			//初始化MAC
			mac.init(restoreSecretKey);
			byte[] bytes = mac.doFinal(data.getBytes());
			encodedMsg = Hex.encodeHexString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  encodedMsg;
	}

}