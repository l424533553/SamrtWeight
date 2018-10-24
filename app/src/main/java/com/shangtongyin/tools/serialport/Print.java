package com.shangtongyin.tools.serialport;

import android.content.Context;
import android.text.TextUtils;

import com.luofx.help.QRHelper;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.util.List;

/**
 * Fun：
 * Author：Linus_Xie
 * Date：2018/8/2 14:22
 */
public class Print {
    public String getPath() {
        return path;
    }

    final byte[] PRINT_Big = new byte[]{0x1d, 0x21, 0x11};//打印大字
    final byte[] PRINT_Big3 = new byte[]{0x1d, 0x21, 0x01};//打印大字(不加宽)
    final byte[] PRINT_Big2 = new byte[]{0x1d, 0x21, 0x10};//打印大字(不加高)
    final byte[] PRINT_Small = new byte[]{0x1d, 0x21, 0x00};//打印正常字
    final byte[] PRINT_Center = new byte[]{0x1B, 0x61, 0x49};//居中
    final byte[] PRINT_Reset = new byte[]{0x1b, 0x40};//重置
    final byte[] PRINT_Wrap = new byte[]{0x0A};//换行  LF
    final byte[] PRINT_Num = new byte[6];//数量

    private String path = "/dev/ttyS1";
    private int baudrate = 9600;

    public android_serialport_api.PosPort mSerialPortBalance = null;
    private boolean isRun;

    public boolean open() {
        try {
            mSerialPortBalance = getSerialPortPrinter();
            if (mSerialPortBalance.getOutputStream() == null || mSerialPortBalance.getInputStream() == null) {
                return false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        isRun = true;
        return true;
    }

    /**
     * 获取端口
     */
    private android_serialport_api.PosPort getSerialPortPrinter() throws SecurityException,
            IOException, InvalidParameterException {
        if (mSerialPortBalance == null) {
            mSerialPortBalance = new android_serialport_api.PosPort(new File(path), baudrate, 0);
        }
        return mSerialPortBalance;
    }

    //关闭
    public void closeSerialPort() {
        if (mSerialPortBalance != null) {
            mSerialPortBalance.close();
            mSerialPortBalance = null;
        }
        mSerialPortBalance = null;
    }

    public void PrintST(Context context) {
        center("====+++++++++++++++++++++++++++++++++====", true);
    }

    /**
     * 打印内容
     *
     * @param msg 打印的消息  ,打完之后跳4行
     * @throws UnsupportedEncodingException 不支持的 编码格式
     */
    public void PrintString(String msg) {
        try {
            if (!TextUtils.isEmpty(msg)) {
                byte[] by = msg.getBytes("GBK");
                getSerialPortPrinter().getOutputStream().write(by);
//            PrintJumpLine((byte) 4);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打字并换行  ,打完之后跳4行
     */
    public void PrintltString(String msg) throws IOException, InterruptedException {
        if (!TextUtils.isEmpty(msg)) {
            byte[] by1 = {27, 51, 0};
            getSerialPortPrinter().getOutputStream().write(by1);
            byte[] by = (msg + "\n").getBytes("GBK");
            getSerialPortPrinter().getOutputStream().write(by);
//            PrintJumpLine((byte) 4);
            Thread.sleep(100);
        }
    }

    /**
     * 重置打印机
     *
     * @throws IOException
     */
    public void reset() throws IOException {
        getSerialPortPrinter().getOutputStream().write(PRINT_Reset);
    }

    /**
     * @param n 用于设置打印字符的方式。默认值是0
     *          位0：保留
     *          位1：1：字体反白
     *          位2：1：字体上下倒置
     *          位3：1：字体加粗
     *          位4：1：双倍高度
     *          位5：1：双倍宽度
     *          位6：1：删除线
     *          位7：1：下划线
     */
    private void setWordMode(byte n) throws IOException {
        byte[] by = {27, 33, n};
        getSerialPortPrinter().getOutputStream().write(by);
    }

    /**
     * 0 不加粗  ，1 加粗
     *
     * @param n 27 69 n
     *          n最低位有效，
     *          等于0时取消字体加粗
     *          非0时设置字体加粗
     * @throws IOException
     */
    public void isWordBlod(byte n) throws IOException {
        byte[] by = {27, 69, n};
        getSerialPortPrinter().getOutputStream().write(by);
    }

    public void PrintNetLine(Context context) {
        byte[] by = {61, 43, 43, 43, 61};
//        resetBalance(PRINT_Wrap);
//        resetBalance(PRINT_Reset);
        resetBalance(PRINT_Small);
        resetBalance(by);
//        resetBalance(PRINT_Wrap);
        resetBalance(by);
    }


    /**
     * 跳行  n 空白
     *
     * @param n 跳 空白的行数
     */
    public void PrintJumpLine(byte n) throws IOException {
        byte[] by = {27, 100, n};
        getSerialPortPrinter().getOutputStream().write(by);
    }


    /**
     * 设置打印机的 打印间隔
     *
     * @param n 0~255  像素点间隔
     */
    public void setLineSpacing(byte n) {
        byte[] by = {27, 51, n};
        try {
            getSerialPortPrinter().getOutputStream().write(by);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印二维码
     */
    public void printQR( byte[] bytes) {

        try {
            //设置间距 为 0
            byte[] by = {27, 51, 0};
            getSerialPortPrinter().getOutputStream().write(by);
//            int width = 32;
//            int height = 32;
//            byte[] bytes = QRHelper.createPixelsQR(message, width, height);
//            List<byte[]> data = QRHelper.createPixelsQR(message, width, height);
//            byte[] bytes = new byte[(32 * 8 + 5) * height];


//            int srcPos = 0;
//            for (int i = 0; i < data.size(); i++) {
////                System.arraycopy(bytes, srcPos, data.get(i), 0, );
//
//                System.arraycopy( data.get(i) , 0,  bytes, srcPos, width * 8 + 5);
//                srcPos += width * 8 + 5;
//
////                getSerialPortPrinter().getOutputStream().write(data.get(i));
////                getSerialPortPrinter().getOutputStream().flush();
////                Thread.sleep(5);
//            }


            getSerialPortPrinter().getOutputStream().write(bytes);
//            getSerialPortPrinter().getOutputStream().write(data.get(i));
            getSerialPortPrinter().getOutputStream().flush();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[]  getbyteData(String message,int  width, int height){
        return QRHelper.createPixelsQR(message, width, height);
    }

    public byte[]  getbyteData2(String message,int  width, int height){
        return QRHelper.createPixelsQR2(message, width, height);
    }

/*
    public void printQR(String message) {
        try {
            //设置间距 为 0
            byte[] by = {27, 51, 0};
            getSerialPortPrinter().getOutputStream().write(by);
            List<byte[]> data = QRHelper.createPixelsQR(message, 32, 32);
            for (int i = 0; i < data.size(); i++) {
                getSerialPortPrinter().getOutputStream().write(data.get(i));
//                Thread.sleep(5);
            }
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }
*/

    public void PrintNetLine2(Context context) throws UnsupportedEncodingException {
        byte[] by = {61, 43, 43, 43, 61};

        byte[] wrap = new byte[]{10, 61, 61, 43, 43, 43, 61, 61, 10};//换行  LF
//      resetBalance(PRINT_Wrap);
//      resetBalance(PRINT_Reset);

////    resetBalance(PRINT_Wrap);
//      resetBalance(wrap);

        String sty = "==你打奶的打得完==\n";
        byte[] s1_byte = sty.getBytes();
        byte[] s2_byte = sty.getBytes("GBK");

        resetBalance(s1_byte);
        resetBalance(s2_byte);

    }


    //居中
    void center(String s1, boolean big) {
        int i = big ? 2 : 1;
        if (big) {
            resetBalance(PRINT_Small);
        } else {
            resetBalance(PRINT_Reset);
        }
        try {
            byte[] s1_byte = s1.getBytes("GBK");
            int length = s1_byte.length;
            if (length < 32 / i) {
                byte[] s2_byte = new byte[32 / i];
                System.arraycopy(s1_byte, 0, s2_byte, (int) ((32 / i - s1_byte.length) / 2), s1_byte.length);
                resetBalance(s2_byte);
            } else {
                resetBalance(s1_byte);
            }
            resetBalance(PRINT_Wrap);
            resetBalance(PRINT_Reset);
            resetBalance(PRINT_Wrap);
            resetBalance(PRINT_Wrap);
            resetBalance(PRINT_Wrap);
            resetBalance(PRINT_Wrap);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置  重新平衡指令
     *
     * @param order 命令指令
     */
    public void resetBalance(byte[] order) {
        for (int i = 0; i < order.length; i++) {
            if (order[i] == 0x00) {
                order[i] = 0x20;
            }
        }

        try {
            getSerialPortPrinter().getOutputStream().write(order);
            //TODO 自己 加的 待验证

            Thread.sleep(100);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
