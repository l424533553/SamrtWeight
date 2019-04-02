package com.luofx.newclass.printer;

import android.text.TextUtils;

import com.axecom.smartweight.my.entity.OrderInfo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
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

    public boolean open() {
        return false;
    }

    protected boolean open(String path, int baudrate) {
        try {
            this.path = path;
            this.baudrate = baudrate;
            serialPort = getSerialPortPrinter();
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
     * 获取端口
     */
    public SerialPort getSerialPortPrinter() throws IOException {
        if (serialPort == null) {
            serialPort = new SerialPort(new File(path), baudrate, 0);
        }
        return serialPort;
    }

    //关闭
    public void closeSerialPort() throws IOException {
        if (serialPort != null) {
            serialPort.getOutputStream().close();
            serialPort.getInputStream().close();
            serialPort.close();
            serialPort = null;
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

    /**
     * bitMatrix 根据 想，x,y 坐标判定是否有 黑点 ，黑点为1 ，白点为0
     */
    private byte getPixels(BitMatrix bitMatrix, int x, int y) {
        if (bitMatrix.get(x, y)) {
            return 1;
            //            return 0xffffff;  //-16777216
        } else {
            return 0;
            //            return  0xffffff;//-1
            //            return 0xff000000;
        }
    }

    /**
     * 将 int[] 转成一个byte  如   int[] arr= {-1,-16777216,-16777216,-1,-1,-1,-16777216,-16777216}  转为 01100011  即为99
     * 获得每行 的数据 ，并确定了bytee数组的长度
     * 8个长度的 int 数组 ，一个字节
     */
    private byte dealArray(byte[] dest) {
        int be = 0;
        int temp;
        for (int j = 0; j < 8; j++) {
            temp = dest[j];
            if (j == 0) {
                be = temp;
            } else {
                be = (be << 1) | temp;
            }
        }
        return (byte) be;
    }

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
     * 根据内容获取 打印二维码的 的各像素点
     */
    private BitMatrix createPixels(String contents, int width, int height) {
        BitMatrix bitMatrix = null;
        //判断URL合法性
        if (contents == null || "".equals(contents) || contents.length() < 1) {
            return null;
        }
        int QR_WIDTH = width * 8;  //  二维码宽
        int QR_HEIGHT = height * 8; //  二维码高

        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//            hints.put(EncodeHintType.ERROR_CORRECTION, "H");// 容错率
        hints.put(EncodeHintType.MARGIN, "1");

        //图像数据转换，使用了矩阵转换
        try {
            bitMatrix = new QRCodeWriter().encode(contents, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitMatrix;
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
        BitMatrix bitMatrix = createPixels(contents1, width, height);
        List<byte[]> bytes = null;
        if (bitMatrix != null) {
            bytes = new ArrayList<>();
            for (int i = height - 1; i >= 0; i--) {
                byte[] rowByte = new byte[maxPix + printHead.length];// 384+4
                System.arraycopy(printHead, 0, rowByte, 0, printHead.length);

                for (int j = 0; j < width * 8; j++) {
                    byte[] dest = new byte[8];
                    dest[0] = getPixels(bitMatrix, i * 8, j);
                    dest[1] = getPixels(bitMatrix, i * 8 + 1, j);
                    dest[2] = getPixels(bitMatrix, i * 8 + 2, j);
                    dest[3] = getPixels(bitMatrix, i * 8 + 3, j);
                    dest[4] = getPixels(bitMatrix, i * 8 + 4, j);
                    dest[5] = getPixels(bitMatrix, i * 8 + 5, j);
                    dest[6] = getPixels(bitMatrix, i * 8 + 6, j);
                    dest[7] = getPixels(bitMatrix, i * 8 + 7, j);
                    rowByte[j + printHead.length] = dealArray(dest);
                }
                bytes.add(rowByte);
            }
        }
        return bytes;
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
        BitMatrix bitMatrix = createPixels(contents1, width, height);
        if (bitMatrix == null) {
            return null;
        }
        ArrayList<byte[]> bytes = new ArrayList<>();
        for (int i = height * 8 - 1; i >= 0; i--) {
            byte[] rowByte = new byte[maxPix / 8 + printHead.length];// 384+4
            System.arraycopy(printHead, 0, rowByte, 0, printHead.length);
            for (int j = 0; j < 48; j++) {
                if (j < width) {
                    int dest = 0;
                    for (int k = 0; k < 8; k++) {
                        dest <<= 1;
                        dest += getPixels(bitMatrix, j * 8 + k, i);
                    }
                    rowByte[j + printHead.length] = (byte) dest;//dealArray(dest);
                }
            }
            bytes.add(rowByte);
        }
        return bytes;
    }


    /**
     * 位图打印的方式   获取打印数据
     * 效率很高（相对于下面）   24*24   位图模式  也有单位图 的打印方式
     * 未创建多余的 打印临时变量 ，比较高效
     *
     * @param contents  打印的内容
     * @param width     宽度  8位 字节长
     * @param height    高度  8位 字节长
     * @param maxPix    最大像素宽
     * @param printHead 位图打印的打印头
     */
    byte[] getRQBitmapData22(String contents, int width, int height, int maxPix, byte[] printHead) {
        BitMatrix bitMatrix = createPixels(contents, width, height);
        if (bitMatrix == null) {
            return null;
        }
        byte[] data = new byte[(maxPix * 3 + printHead.length) * height / 3];
        int k = 0;
        for (int j = height / 3 - 1; j >= 0; j--) {
            System.arraycopy(printHead, 0, data, k, printHead.length);
            k += printHead.length;

            for (int i = 0; i < width * 8; i++) {
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 8; n++) {
                        byte b = getPixels(bitMatrix, i, j * 24 + m * 8 + n);//将 8 个0,1 byte 转成一个字节
                        data[k] += data[k] + b;
                    }
                    k++;
                }
            }
            k = k + (maxPix - width * 8) * 3;// 补充空格
        }
        return data;
    }

    byte[] getRQBitmapData(String contents, int width, int height, int maxPix, byte[] printHead) {
        BitMatrix bitMatrix = createPixels(contents, width, height);
        if (bitMatrix == null) {
            return null;
        }
        byte[] data = new byte[(maxPix * 3 + printHead.length + 1) * height / 3];
        int k = 0;
        for (int j = 0; j < height / 3; j++) {
            System.arraycopy(printHead, 0, data, k, printHead.length);
            k += printHead.length;
            for (int i = 0; i < width * 8; i++) {
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 8; n++) {
                        byte b = getPixels(bitMatrix, i, j * 24 + m * 8 + n);//将 8 个0,1 byte 转成一个字节
                        data[k] += data[k] + b;
                    }
                    k++;
                }
            }
            k = k + (maxPix - width * 8) * 3;// 补充空格
            data[k] = 10;
        }
        return data;
    }

    List<byte[]> getRQBitmapDataCopy(String contents, int width, int height, int maxPix, byte[] printHead) {
        List<byte[]> bytes = new ArrayList<>();
        BitMatrix bitMatrix = createPixels(contents, width, height);
        if (bitMatrix == null) {
            return null;
        }

        for (int j = 0; j < height / 3; j++) {
            byte[] data = new byte[maxPix * 3 + printHead.length];
            System.arraycopy(printHead, 0, data, 0, printHead.length);
            int k = printHead.length;
            for (int i = 0; i < width * 8; i++) {
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 8; n++) {
                        byte b = getPixels(bitMatrix, i, j * 24 + m * 8 + n);//将 8 个0,1 byte 转成一个字节
                        data[k] += data[k] + b;
                    }
                    k++;
                }
            }
//            data[data.length - 1] = 10;
            bytes.add(data);
        }
        return bytes;
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
        BitMatrix bitMatrix = createPixels(contents, width, height);
        if (bitMatrix == null) {
            return null;
        }

        List<byte[]> bytes = new ArrayList<>();
        for (int j = 0; j < height; j++) {
            int k = 0;
            byte[] data = new byte[maxPix + printHead.length];
            System.arraycopy(printHead, 0, data, k, printHead.length);

            for (int i = 0; i < width * 8; i++) {
                byte[] dest = new byte[8];
                dest[0] = getPixels(bitMatrix, i, j * 8);
                dest[1] = getPixels(bitMatrix, i, j * 8 + 1);
                dest[2] = getPixels(bitMatrix, i, j * 8 + 2);
                dest[3] = getPixels(bitMatrix, i, j * 8 + 3);
                dest[4] = getPixels(bitMatrix, i, j * 8 + 4);
                dest[5] = getPixels(bitMatrix, i, j * 8 + 5);
                dest[6] = getPixels(bitMatrix, i, j * 8 + 6);
                dest[7] = getPixels(bitMatrix, i, j * 8 + 7);
                data[i + printHead.length] = dealArray(dest);
            }
            bytes.add(data);
        }
        return bytes;
    }

    public abstract void printOrder(ExecutorService executorService, final OrderInfo orderInfo);

    //TODO 上面的  同样方法，没啥用
//    /**
//     * 位图打印的方式
//     * 效率很高（相对于下面）   24*24   位图模式
//     * @return
//     */
//    public List<byte[]> draw2PxPoint(BitMatrix bitMatrix, int width, int height) {
//        List<byte[]> bytes = new ArrayList<>();
//        for (int j = 0; j < height / 3; j++) {
//            byte[] data = new byte[48 * 8 * 3 + 5];
//            int k = 0;
//            data[k++] = 27;
//            data[k++] = 42;
//            data[k++] = 33; // m=33时，选择24点双密度打印，分辨率达到200DPI。
//            data[k++] = (byte) 0x80;
//            data[k++] = 0x01;
//
//            for (int i = 0; i < width * 8; i++) {
////                if(i<width*8){
//                for (int m = 0; m < 3; m++) {
//                    byte[] dest = new byte[8];
//                    for (int n = 0; n < 8; n++) {
//                        dest[n] = getPixels(bitMatrix, i, j * 24 + m * 8 + n);
//                    }
//                    data[k++] = changePointPx(dest);
//                }
////                }
//            }
//            bytes.add(data);
//        }
//        return bytes;
//    }

}
