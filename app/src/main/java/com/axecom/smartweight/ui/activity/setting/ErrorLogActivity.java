package com.axecom.smartweight.ui.activity.setting;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.luofx.utils.log.MyLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ErrorLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_log);
        TextView textError = findViewById(R.id.textError);

        String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/log/error" + ".txt";
        File file = new File(fileName);
        if (file.exists()) {
            String error = readFileByLines(fileName);
            MyLog.blue(error);
            textError.setText(error);
        }
    }

    /**
     * 读取文件内容
     * @param args
     */
    public static void main(String[] args) {
        //InputStream:是一个抽象类
        // \:是一个 转移符
        //表示磁盘路径的两种表示方式：1、\\   2、/
        try {
            //从文件地址中读取内容到程序中
            //1、建立连接
            InputStream is = new FileInputStream("E:/iodemo/ch01.txt");
            //2、开始读取信息
            //先定义一个字节数组存放数据
            byte[] b = new byte[5];//把所有的数据读取到这个字节当中
            //完整的读取一个文件
            is.read(b);
            //read:返回的是读取的文件大小
            //最大不超过b.length，返回实际读取的字节个数
            System.out.println(Arrays.toString(b));//读取的是字节数组
            //把字节数组转成字符串
            System.out.println(new String(b));
            //关闭流
            is.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            //系统强制解决的问题：文件没有找到
            e.printStackTrace();
        } catch (IOException e) {
            //文件读写异常
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            String tempString = null;
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return sb.toString();
    }

}
