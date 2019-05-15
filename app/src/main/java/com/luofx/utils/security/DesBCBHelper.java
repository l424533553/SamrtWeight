package com.luofx.utils.security;


import android.annotation.SuppressLint;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/***
 * DES ECB PKCS5Padding 对称加密 解密
 *
 * 作者：罗发新
 * 时间：2019/1/15 0015    9:40
 * 邮件：424533553@qq.com
 * 说明：
 */
public class DesBCBHelper {

    private static DesBCBHelper mInstants;

    public static DesBCBHelper getmInstants() {
        if (mInstants == null) {
            mInstants = new DesBCBHelper();
        }
        return mInstants;
    }

    private DesBCBHelper() {
        try {
            init();
        } catch (InvalidKeyException  e) {
            e.printStackTrace();
        }
    }

    private AlgorithmParameterSpec iv = null;// 加密算法的参数接口，IvParameterSpec是它的一个实现
    private SecretKey key = null;

    private void init() throws InvalidKeyException {
        String desKey = "data.axebao.com@axecom.s";
        String desIv = "@smh2019";
        byte[] DESkey = desKey.getBytes();
        byte[] DESIV = desIv.getBytes();
        DESKeySpec keySpec = new DESKeySpec(DESkey);// 设置密钥参数
        iv = new IvParameterSpec(DESIV);// 设置向量
//        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
//        key = keyFactory.generateSecret(keySpec);// 得到密钥对象
        key = new SecretKeySpec(DESkey, "DESede");
    }

    /**
     * 加密
     *
     * @param data 待加密的数据
     * @return 加密后的数据
     */
    public String encode(String data) {
        String encodeString = null;
        try {
            Cipher enCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");// 得到加密对象Cipher
            enCipher.init(Cipher.ENCRYPT_MODE, key, iv);// 设置工作模式为加密模式，给出密钥和向量
            byte[] pasByte = enCipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            encodeString = bytesToHexString(pasByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeString;
    }

    /**
     * 計量院的加密設計
     */
    public String encodeECB() {
        String data = "service=sign&cmd=login&authenCode=fpms_vender_axb&password=h79OpV3MtCfiZHcu";
        String encodeString = null;
        try {
            String desKey = "F7AD4703F4520AFDB0216339";
            byte[] DESkey = desKey.getBytes();
            SecretKeySpec mainkey = new SecretKeySpec(DESkey, "DESede");
            @SuppressLint("GetInstance")
            Cipher enCipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");// 得到加密对象Cipher
            enCipher.init(Cipher.ENCRYPT_MODE, mainkey);// 设置工作模式为加密模式，给出密钥和向量
            byte[] pasByte = enCipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            encodeString = bytesToHexString(pasByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeString;
    }

    /// <summary>
    /// utf-8编码
    /// 加密模式ECB，填充类型PKCS7
    /// </summary>
    /// <param name="str_content"></param>
    /// <param name="str_keys">24位key</param>
    /// <returns></returns>
//    public static String DES3_Encrypt(String str_content, String str_keys) {
//        Encoding encoding = Encoding.UTF8;
//
//        byte[] content = encoding.GetBytes(str_content);
//        byte[] keys = encoding.GetBytes(str_keys);
//
//        TripleDESCryptoServiceProvider tdsc = new TripleDESCryptoServiceProvider();
//
//        //指定密匙长度，默认为192位
//        tdsc.KeySize = 128;
//        //使用指定的key和IV（加密向量）
//        tdsc.Key = keys;
//        //tdsc.IV = IV;
//        //加密模式，偏移
//        tdsc.Mode = CipherMode.ECB;
//        tdsc.Padding = PaddingMode.PKCS7;
//        //进行加密转换运算
//        ICryptoTransform ct = tdsc.CreateEncryptor();
//        //8很关键，加密结果是8字节数组
//        byte[] results = ct.TransformFinalBlock(content, 0, content.Length);
//
//        string base64String = Convert.ToBase64String(results);
//        return base64String;
//    }

    /**
     * @param bArray 加密的字节数组
     */
    private String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (byte aBArray : bArray) {
            sTemp = Integer.toHexString(0xFF & aBArray);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp);
        }
        return sb.toString();
    }

//    public static void main(String[] args) throws Exception {
//        String clearText = "marketid=11&terid=103&flag=0";
//        DesBCBHelper helper = new DesBCBHelper();
//        System.out.println("明文：" + clearText);
//        String encryptText = helper.encode(clearText);
//        System.out.println("加密后：" + encryptText);
////        String decryptText = decryptDES(encryptText, key);
////        System.out.println("解密后：" + decryptText);
//    }
}
