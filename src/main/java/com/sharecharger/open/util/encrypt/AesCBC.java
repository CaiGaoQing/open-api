package com.sharecharger.open.util.encrypt;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 *
 * 蔡高情
 *
 */

/**
 * Copyright (C), 2018-2018,
 * FileName: AesCBC
 * Author:   $蔡高情
 * Date:     2018/7/5 20:06
 * Description: AesCBC加密解密
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名  蔡高情       postBody数据加密
 */
public class AesCBC {
    /*
    * 此处使用AES-128-CBC加密模式，key需要为16位。
    */

    private static AesCBC instance=null;
    private AesCBC(){

    }
    public static AesCBC getInstance(){
        if (instance==null){
            instance= new AesCBC();
        }
        return instance;
    }

    // 加密
    public String encrypt(String sSrc, String sKey, String ivParameter) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码。
}

    // 解密
    public String decrypt(String sSrc, String sKey, String ivParameter) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original,"utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

}