package com.axecom.smartweight.helper.printer;

import android.text.TextUtils;

import com.axecom.smartweight.entity.project.OrderInfo;
import com.xuanyuan.library.help.qr.MyQRHelper;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutorService;

import android_serialport_api.SerialPort;

/**
 * 基本的模板  ，二维码 内容是可以支持 UTF-8和 gbk 两种编码方式的
 * 打印机将支持  炜煌 和爱普生
 */
public abstract class MyBasePrinter {

    /**
     * 是否不打印二维码
     */
    protected boolean isNoQR;

    public void setNoQR(boolean noQR) {
        isNoQR = noQR;
    }

    private SerialPort serialPort = null;

    public SerialPort getSerialPort() {
        return serialPort;
    }

    protected String path;
    protected int baudrate;

    public abstract void printBitmapQR(String contents1, int width, int height, int maxPix);


    public abstract boolean open();

    protected boolean open(String path, int baudrate) {
        try {
            this.path = path;
            this.baudrate = baudrate;
            serialPort = getSerialPortPrinter();
            if (serialPort.getOutputStream() != null && serialPort.getInputStream() != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 获取端口
     */
    public SerialPort getSerialPortPrinter() throws IOException {
        if (serialPort == null) {
            serialPort = new SerialPort(new File(path), baudrate, 0);
        }
        return serialPort;
    }

    //关闭
    public void closeSerialPort() {
        try {
            if (serialPort != null) {
                serialPort.close();
                serialPort.getOutputStream().close();
                serialPort.getInputStream().close();
                serialPort = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected int write(byte[] by) {
        try {
            getSerialPortPrinter().getOutputStream().write(by);
            return by.length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    protected void flush() {
        try {
            getSerialPortPrinter().getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void write(List<byte[]> bytes) {
        try {
            for (int i = 0; i < bytes.size(); i++) {
                Thread.sleep(30);
                write(bytes.get(i));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印内容
     *
     * @param msg 打印的消息  ,打完之后跳4行
     */
    public int printString(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            try {
                byte[] by = msg.getBytes("GBK");
                write(by);
                flush();
                return by.length;
            } catch (UnsupportedEncodingException e) {//UnsupportedEncodingException 不支持的 编码格式
                e.printStackTrace();
            }
        }
        return 0;
    }

    public void printString(String msg, byte spaceLine) {
        if (!TextUtils.isEmpty(msg)) {
            try {
                byte[] by = msg.getBytes("GBK");
                write(by);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 打字并换行  ,打完之后跳4行
     */
    public void printltString(String msg) {
        try {
            if (!TextUtils.isEmpty(msg)) {
                byte[] by = (msg + "\n").getBytes("GBK");
                write(by);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置打印机的 打印间隔
     *
     * @param n 0~255  像素点间隔
     */
    public abstract void setLineSpacing(byte n);
//    {
//        byte[] by = {27, 51, n};
//        try {
//            getSerialPort().getOutputStream().write(by);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    protected abstract void readyPrinterQR();

    public abstract void finishPrinterQR(byte n);


    // 将[1,0,0,1,0,0,0,1]这样的二进制转为化十进制的数值 的byte
    public int changePointPx1(byte[] arry) {
        int v = 0;
        for (int j = 0; j < arry.length; j++) {
            if (arry[j] == 1) {
                v = v | 1 << j;
            }
        }
        return v;
    }

    /**
     * 将[1,0,0,1,0,0,0,1]这样的二进制转为化十进制的数值 的byte
     *
     * @param arry 需要转换的数组
     * @return 返回
     */
    public byte changePointPx(byte[] arry) {
        byte v = 0;
        for (int i = 0; i < 8; i++) {
            v += v + arry[i];
        }
        return v;
    }

    /**
     * 对齐方式
     *
     * @param n 0 左对齐 ；  1 居中  ； 2 右对齐
     */
    protected void alignment(byte n) {
        byte[] by = {27, 97, n};
        try {
            getSerialPortPrinter().getOutputStream().write(by);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 普通点阵打印数据
     * 其实也可以将数据  放在一个byte[] 中
     *
     * @param contents1 打印的内容
     * @param width     宽度  8位 字节长
     * @param height    高度  8位 字节长
     */
    List<byte[]> getRQMatrixData(String contents1, int width, int height, int maxPix, byte[] printHead) {
        return MyQRHelper.getRQMatrixData(contents1, width, height, maxPix, printHead);
    }

    /**
     * 点行打印  二维码
     *
     * @param contents1 打印内容
     * @param width     宽度
     * @param height    高度
     * @return 返回打印数据
     */
    public List<byte[]> createPixelsPointRow(String contents1, int width, int height, int maxPix, byte[] printHead) {
        return MyQRHelper.createPixelsPointRow(contents1, width, height, maxPix, printHead);
    }

    byte[] getRQBitmapData(String contents, int width, int height, int maxPix, byte[] printHead) {
        return MyQRHelper.getRQBitmapData(contents, width, height, maxPix, printHead);
    }

    List<byte[]> getRQBitmapDataCopy22(String contents, int width, int height, int maxPix, byte[] printHead) {
        return MyQRHelper.getRQBitmapDataCopy22(contents, width, height, maxPix, printHead);
    }


    /**
     * 位图打印的方式   获取打印数据
     * 效率很高（相对于下面）   8*8   位图模式  也有单位图 的打印方式
     * 数据命令不一样 ，处理方式 也会不一样
     * <p>
     * 未创建多余的 打印临时变量 ，比较高效
     *
     * @param contents  打印的内容
     * @param width     宽度  8位 字节长
     * @param height    高度  8位 字节长
     * @param maxPix    最大像素宽
     * @param printHead 位图打印的打印头
     */
    public List<byte[]> getRQBitmap88Data(String contents, int width, int height, int maxPix, byte[] printHead) {
        return MyQRHelper.getRQBitmap88Data(contents, width, height, maxPix, printHead);
    }

    public abstract void printOrder(ExecutorService executorService, final OrderInfo orderInfo);

}
