package com.axecom.smartweight.helper.weighter;

import android.util.Log;

import com.axecom.smartweight.config.IEventBus;
import com.axecom.smartweight.entity.system.BaseBusEvent;
import com.xuanyuan.library.utils.text.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Arrays;


/**
 * 香山秤15.6 黑色屏
 */
public class SXWeighter extends MyBaseWeighter implements IEventBus {
    private static SXWeighter mInstance;


    public static SXWeighter getXSWeighter() {
        return mInstance;
    }

    public SXWeighter() {
        mInstance = this;
    }

    public static final String TAG = "SXWeighter";
    private ReadThread readThread;

    /**
     * 打开串口,数据功能
     */
    public boolean open() {
        int baudrate = 9600;
        String path = "/dev/ttyS3";
        if (!super.open(path, baudrate)) {
            return false;
        }
        readThread = new ReadThread();
        readThread.start();//开始线程监控是否有数据要接收
        //new SendThread().start(); //开始线程发送指令
        return true;
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        if (readThread != null) {
            readThread.stopRun();
        }
        super.closeSerialPort();
    }

    /**
     * 设置重置按钮  ，去皮命令
     */
    public void resetBalance() {
        byte[] sendData = {(byte) 0xC4, 0x16, 0x01, 0x00, 0x00, 0x00};
        write(sendData);
    }

    // 获取K值
    @Override
    public void getKValue() {
        String zer = "<QUE >";
        sendString(zer);
    }

    @Override
    public void readyBD() {
        byte[] sendData = {(byte) 0xAA, 0x01, 0x02, 0x00, 0x00, 0x00, 0x00, (byte) 0xAE};
        write(sendData);
    }

    /**
     * 置零命令
     * private String wei = "<WEI >";  称重指令
     * private String zer = "<ZER >";  置零指令
     * private String tar = "<TAR >";  去皮指令
     * 发送串口指令
     */
    @Override
    public void sendZer() {

//        AA 01 04 00 00 00 00 B0
        byte[] sendData = {(byte) 0xAA, 0x01, 0x04, 0x00, 0x00, 0x00, 0x00, (byte) 0xB0};
        write(sendData);
//        flush();
    }


    /**
     * private String wei = "<WEI >";  称重指令
     * private String zer = "<ZER >";  置零指令
     * private String tar = "<TAR >";  去皮指令
     * 发送串口指令
     */
    private void sendString(String data) {
        byte[] sendData = data.getBytes(); //string转byte[]
        if (sendData.length > 0) {
            write(sendData);
            flush();
        }
    }


    /**
     * 去除拆机标识位
     */
    public void sendRemoveSign() {
        String zer = "<PSD ";
        byte[] sendData = zer.getBytes(); //string转byte[]
        write(sendData);
        flush();
    }

    /**
     * 发送数据
     */
    public void sendByteData() {
        byte[] sendData = {0x3C, 0x43, 0x41, 0x4C, 0x30, 0x30, 0x32, 0x30, (byte) 0x92, 0x3E};
        write(sendData);
        flush();
    }

    /**
     * 发送 标定数据  ,10kg 和20kg的两种标定方法
     *
     * @param isCalibration10kg 开始标定数据  true:是 10kg标定方式  ，false 20kg标定方式
     */
    @Override
    public void sendCalibrationData(boolean isCalibration10kg) {
        byte[] sendData;
        if (isCalibration10kg) {//10kg的标定法。
            sendData = new byte[]{(byte) 0xAA, 0x01, 0x03, 0x0A, 0x00, 0x05, 0x00, (byte) 0xBE};
        } else {
            sendData = new byte[]{(byte) 0xAA, 0x01, 0x03, 0x0A, 0x00, 0x05, 0x00, (byte) 0xBE};
        }
        write(sendData);
        flush();
    }

    private class ReadThread extends Thread {
        private boolean isRun; //是否一直循环

        @Override
        public synchronized void start() {
            isRun = true;
            super.start();
        }

        private void stopRun() {
            isRun = false;
            interrupt();
        }

        /*       */

        /**
         * 准备状态
         *//*
        private void readyState(int byte_data) {
            switch (byte_data) {
                case '<':
                    *//* *
         * 单开一线程，来读数据
         * currentAd = array[0];
         * zeroAd = array[1];
         * currentWeight = array[2];
         * tareWeight = array[3];
         * <p>
         * cheatSign = array[4];
         * isNegative = array[5];
         * isOver = array[6];
         * isZero = array[7];
         * isPeeled = array[8];
         * isStable = array[9];
         *//*
                    // 状态
                    int rc_state = 1;   //返回的数据，包括电池，和命令返回状态
                    break;
                case '#':
                    break;
                case 10:            //可能是称重数据
                    break;
                case 13:            //称重数据
                    //当前AD
                    break;
                default:        //当做标定参数
                    //砝码AD
                    break;
            }
        }*/
        @Override
        public void run() {
            super.run();
            //判断进程是否在运行，更安全的结束进程
            byte[] data = new byte[40];
            boolean dataFinish = true;
            int dataLength = 0;
            try {
                while (isRun) {
                    byte[] buffer = new byte[20];
                    int size; //读取数据的大小
                    size = getSerialPort().getInputStream().read(buffer, 0, buffer.length);
                    String content = StringUtils.bytesToHexString(buffer);
                    Log.i("gggg", "数据" + content);
                    Log.i("gggg", "数据" + Arrays.toString(buffer));
                    // 使用情况，使用情况，


                    if (size >= 19) {
                        if (content.length() >= 38) {
                            if (content.startsWith("AAFE")) {
                                BaseBusEvent event = new BaseBusEvent();
                                event.setEventType(WEIGHT_SX);
                                event.setOther(content);
                                EventBus.getDefault().post(event);
                            }
                        }
                    }


//                    if (buffer[0] == -60) {
//                        System.arraycopy(buffer, 0, data, 0, size);
//                        dataLength = size;
//                        dataFinish = false;
//                    } else {//数据中断区
//                        if (!dataFinish) {
//                            System.arraycopy(buffer, 0, data, dataLength, 40 - dataLength);
//                            dataFinish = true;
//                        }
//                    }
//
//                    if (dataFinish) {
//                        // 数据接收完全可以发数据了
//                        if (data[1] == 4 || data[1] == 5) {
//                            // 数据完整
//                            sendReadMessage(data, WEIGHT_AXE);
////                                MyLog.log("称重数据===" + Arrays.toString(data));
//                        }
//                    }

                    Thread.sleep(80);
                }
            } catch (IOException e) {
                Log.e(TAG, "run: 数据读取异常：" + e.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param str       服务内容
     * @param eventType 事件类型 ，监听者注册后
     */
    private void sendReadMessage(String str, String eventType) {
        BaseBusEvent event = new BaseBusEvent();
        event.setEventType(eventType);
        event.setOther(str);
        EventBus.getDefault().post(event);
    }

    private void sendReadMessage(byte[] str, String eventType) {
        BaseBusEvent event = new BaseBusEvent();
        event.setEventType(eventType);
        event.setOther(str);
        EventBus.getDefault().post(event);
    }

    /***
     * 称重的有效数据 一般是 57位
     *
     * @param b  源数组
     * @param off  起始位置
     * @param len  数组 b 的数据长度length
     * @return 返回InputStream 中的byte 数据长度
     * @throws IOException  抛出的异常
     *
     * int 返回buffer 的 数据长度
     */
    public int read22(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            return -1;
        } else if (off < 0 || len < 0 || len > b.length - off) {
            return -1;
        } else if (len == 0) {
            return 0;
        }

        int c = getSerialPort().getInputStream().read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte) c;

        int i = 1;
        for (; i < len; i++) {
            c = getSerialPort().getInputStream().read();
            if (c == -1) {
                break;
            }
            if (c < 0 || c > 127) {
                b[off + i] = 0;
            } else {
                b[off + i] = (byte) c;
            }

        }
        return i;
    }

    /**
     * 单开一线程，来发数据
     */
    private class SendThread extends Thread {
        private boolean isRun; //是否一直循环

        @Override
        public synchronized void start() {
            isRun = true;
            super.start();
        }

        private void stopRun() {
            isRun = false;
            interrupt();
        }

        @Override
        public void run() {
            super.run();
            //判断进程是否在运行，更安全的结束进程
            while (!isRun) {
                Log.d(TAG, "进入线程run");
                try {
                    sendString("<WEI >");
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 测试，使用情况。
     */
    private void test() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {


            }
        };
    }


}

////10kg
//        AAFE004651B00006243B00465199000A00059400
//        -86, -2, 0, 70, 81, -80, 0, 6, 36, 59, 0, 70, 81, -103, 0, 10, 0, 5, -108, 0]
//
//        AAFE004651B80006243B00465199000A00059C00
//        -86, -2, 0, 70, 81, -72, 0, 6, 36, 59, 0, 70, 81, -103, 0, 10, 0, 5, -100, 0
//
//0kg  **************************************
//        AAFE000623F80006243B00465199000A00056E00
//        -86, -2, 0, 6, 35, -8, 0, 6, 36, 59, 0, 70, 81, -103, 0, 10, 0, 5, 110, 0]


//
//5kg ************************
//        AAFE00263B160006243B00465199000A0005C400
//        -86, -2, 0, 38, 59, 22, 0, 6, 36, 59, 0, 70, 81, -103, 0, 10, 0, 5, -60, 0]
//
//        [00 07 04 AB](459947) [00 06 EF 1C] [00 47 BC 70] [00 0A 00 05]
//
//  0kg:      AAFE 000623F8/402424    0006243B/402491  00465199/4608409  000A0005/655365  6E00
//  5kg:      AAFE 00263B16/2505494   0006243B/402491  00465199/4608409  000A0005/655365  C400
//  10k:      AAFE 004651B0/4608432   0006243B/402491  00465199/4608409  000A0005/655365  9400

//  标定秤之前
//  使用，   66-42=24*5=120  +220=340    380 ，  4954.

//                             AAFE  004650F2        0006243B         00465199         000A0005        D500
//                             AAFE  00062610        0006243B         00465199         000A0005        8900
//                             AAFE  0048A86B        0006243B         00465199         000A0005        A800

// 使用 情况  情况。

