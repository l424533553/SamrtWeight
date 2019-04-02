package com.luofx.newclass.weighter;

import android.os.Handler;

import java.io.File;
import java.io.IOException;

import android_serialport_api.SerialPort;

/**
 * 作者：罗发新
 * 时间：2018/12/6 0006    17:06
 * 邮件：424533553@qq.com
 * 说明：
 */
public abstract class MyBaseWeighter {
//    protected Handler handler;
    private  String path;
    private  int baudrate;
    private  SerialPort serialPort;

    public boolean open() {
        return false;
    }

    protected boolean open(String path, int baudrate) {
        try {
            this.path = path;
            this.baudrate = baudrate;
            serialPort = getSerialPort();
            if (serialPort.getOutputStream() == null || serialPort.getInputStream() == null) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 获得打印机的串口 控制
     *
     * @return 打印机串口
     */
    public  SerialPort getSerialPort() {
        try {
            if (serialPort == null) {
                serialPort = new SerialPort(new File(path), baudrate, 0);
            }
            return serialPort;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //关闭
    public void closeSerialPort() {
        if (serialPort != null) {
            try {
                serialPort.getOutputStream().close();
                serialPort.getInputStream().close();
//                serialPort.close();

                serialPort = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void write(byte[] by) {
        try {
            getSerialPort().getOutputStream().write(by);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void flush() {
        try {
            getSerialPort().getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public abstract void resetBalance();

    public abstract void getKValue();

}
