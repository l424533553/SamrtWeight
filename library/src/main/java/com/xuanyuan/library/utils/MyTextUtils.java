package com.xuanyuan.library.utils;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import static com.xuanyuan.library.utils.text.StringUtils.bytesToHex;

/**
 * author: luofaxin
 * date： 2018/10/24 0024.
 * email:424533553@qq.com
 * describe: 关于textView 的便捷工具类
 */
public class MyTextUtils {

    /**
     * 转换成GBK
     *
     * @param str 字符串
     */
    public static String changeCharset(String str) {
        if (str != null) {
            //用默认字符编码解码字符串。
            byte[] bs = str.getBytes();
            //用新的字符编码生成字符串
            return new String(bs, StandardCharsets.UTF_8);
        }
        return null;
    }

    /**
     * 清空文件内容
     */
    public static void clearInfoForFile(String fileName) {
        File file = new File(fileName);
        try {
            if (file.exists()) {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write("*********************暂无数据**********************");
                fileWriter.flush();
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //生成MD5
    public static String getMD5(String message) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");  // 创建一个md5算法对象
            byte[] messageByte = message.getBytes(StandardCharsets.UTF_8);
            byte[] md5Byte = md.digest(messageByte);              // 获得serialPort字节数组,16*8=128位
            md5 = bytesToHex(md5Byte);                            // 转换为16进制字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }
}
