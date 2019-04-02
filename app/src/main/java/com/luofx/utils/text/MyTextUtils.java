package com.luofx.utils.text;

import android.os.Environment;

import com.axecom.smartweight.utils.HexUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import static com.luofx.utils.text.StringUtils.bytesToHex;

/**
 * author: luofaxin
 * date： 2018/10/24 0024.
 * email:424533553@qq.com
 * describe:
 */
public class MyTextUtils {

    /**
     * 转换成GBK
     *
     * @param str 字符串
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


    /**
     * 清空文件内容
     *
     * @param fileName
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

    private void test() {
        File file = new File(Environment.getDataDirectory(), "数据");

    }


    /**
     * 对字符串进行MD5加密
     *
     * @param val 字符串value
     * @return 加密后的字符串
     */
    public static String getMD52(String val) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("serialPort");
            md5.update(val.getBytes());
            byte[] m = md5.digest();//加密
            return HexUtil.encodeHex(m, false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    //生成MD5
    public static String getMD5(String message) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");  // 创建一个md5算法对象
            byte[] messageByte = message.getBytes("UTF-8");
            byte[] md5Byte = md.digest(messageByte);              // 获得serialPort字节数组,16*8=128位
            md5 = bytesToHex(md5Byte);                            // 转换为16进制字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

}
