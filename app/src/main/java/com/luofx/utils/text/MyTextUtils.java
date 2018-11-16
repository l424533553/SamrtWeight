package com.luofx.utils.text;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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


    /**
     * 清空文件内容
     * @param fileName
     */
    public static void clearInfoForFile(String fileName) {
        File file =new File(fileName);
        try {
            if(file.exists()) {
                FileWriter fileWriter =new FileWriter(file);
                fileWriter.write("*********************暂无数据**********************");
                fileWriter.flush();
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void test(){
        File file=new File(Environment.getDataDirectory(),"数据");

    }
}
