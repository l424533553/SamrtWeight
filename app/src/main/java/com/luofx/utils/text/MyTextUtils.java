package com.luofx.utils.text;

import java.io.UnsupportedEncodingException;

/**
 * author: luofaxin
 * date： 2018/10/24 0024.
 * email:424533553@qq.com
 * describe:
 */
public class MyTextUtils {

    /**
     * 转换成GBK
     * @param str  字符串
     * @return
     */
    public static String changeCharset(String str) {
        try {
            if (str != null) {
                //用默认字符编码解码字符串。
                byte[] bs = str.getBytes();
                //用新的字符编码生成字符串
                return new String(bs, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
