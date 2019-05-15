package com.axecom.smartweight.utils;

import android.annotation.SuppressLint;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

///** AES对称加密解密类 **/

/**
 * DESede/ECB/PKCS5Padding  加密方式不是语义上安全的  ，
 * 推荐使用 DESede/CBC/PKCS5Padding
 */
public class AESHelper {
    ///** 创建密钥 **/
    private static SecretKeySpec createKey(String password) {
        byte[] data;
        if (password == null) {
            password = "";
        }
        StringBuilder sb = new StringBuilder(32);
        sb.append(password);
        while (sb.length() < 32) {
            sb.append("0");
        }
        if (sb.length() > 32) {
            sb.setLength(32);
        }

        data = sb.toString().getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(data, "AES");
    }

    // /** 加密字节数据 **/
    @SuppressLint("GetInstance")
    public static byte[] encrypt(byte[] content, String password) {
        try {
            // /** 算法/模式/填充 **/
            String CipherMode = "AES/ECB/PKCS5Padding";
            SecretKeySpec key = createKey(password);
            System.out.println(key);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    ///** 加密(结果为16进制字符串) **/
    public static String encrypt(String content, String password) {
        byte[] data = null;
        try {
            data = content.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = encrypt(data, password);
        if (data != null) {
            return byte2hex(data);
        }
        return null;

    }

    // /** 解密字节数组 **/
    @SuppressLint("GetInstance")
    public static byte[] decrypt(byte[] content, String password) {
        try {
            // /** 算法/模式/填充 **/
            String CipherMode = "AES/ECB/PKCS5Padding";
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    ///** 解密16进制的字符串为字符串 **/
    public static String decrypt(String content, String password) {
        byte[] data = null;
        try {
            data = hex2byte(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = decrypt(data, password);
        if (data == null)
            return null;
        String result;
        result = new String(data, StandardCharsets.UTF_8);
        return result;
    }

    // /** 字节数组转成16进制字符串 **/
    private static String byte2hex(byte[] b) { // 一个字节的数，
        StringBuilder sb = new StringBuilder(b.length * 2);
        String tmp;
        for (byte b1 : b) {
            // 整数转成十六进制表示
            tmp = (Integer.toHexString(b1 & 0XFF));
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return sb.toString().toUpperCase(); // 转成大写
    }

    // /** 将hex字符串转换成字节数组 **/
    private static byte[] hex2byte(String inputString) {
        if (inputString == null || inputString.length() < 2) {
            return new byte[0];
        }
        inputString = inputString.toLowerCase();
        int l = inputString.length() / 2;
        byte[] result = new byte[l];
        for (int i = 0; i < l; ++i) {
            String tmp = inputString.substring(2 * i, 2 * i + 2);
            result[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
        }
        return result;
    }

    /**
     * 采用3DES 加密  Android 中默认的加密方法就是 ECB模式，该模式事实上是不安全的
     * ECB模式中  只有PKCS5Padding 模式在Android中被支持，PKCS7Padding会报错
     *
     * @param src 加密数据源
     * @param key 加密秘钥
     * @return 返回16进制的加密数据
     * 数据格式异常 和 转换异常
     */
    @SuppressLint("GetInstance")
    public static String encryptDESedeECB(final String src, final String key) {
        try {
            final DESedeKeySpec dks = new DESedeKeySpec(key.getBytes(StandardCharsets.UTF_8));
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            final SecretKey securekey = keyFactory.generateSecret(dks);

            String transformation = "DESede/ECB/PKCS5Padding";
            final Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, securekey);
            byte[] b = cipher.doFinal(src.getBytes());
            return byte2hex(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

}