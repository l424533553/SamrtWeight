package com.axecom.smartweight.helper.weighter;

import android.util.Log;

import com.axecom.smartweight.config.IEventBus;
import com.axecom.smartweight.entity.system.BaseBusEvent;
import com.xuanyuan.library.MyLog;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Arrays;

/**
 * 香山秤15.6 黑色屏
 */
public class AXEWeighter extends MyBaseWeighter implements IEventBus {
    private static AXEWeighter mInstance;

    public static AXEWeighter getXSWeighter() {
        return mInstance;
    }

    public AXEWeighter() {
        mInstance = this;
    }

    public static final String TAG = "XSWeighter15";
    private ReadThread readThread;

    /**
     * 打开串口,数据功能
     */
    public boolean open() {
        int baudrate = 38400;
        String path = "/dev/ttySAC3";
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

    /**
     * 置零命令
     * private String wei = "<WEI >";  称重指令
     * private String zer = "<ZER >";  置零指令
     * private String tar = "<TAR >";  去皮指令
     * 发送串口指令
     */
    @Override
    public void sendZer() {
        byte[] sendData = {(byte) 0xC4, 0x15, 0x01, 0x00, 0x00, 0x00};
        write(sendData);
//        flush();
    }

    @Override
    public void readyBD() {

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
        if (isCalibration10kg) {
            sendData = new byte[]{(byte) 0xC4, 0x13, 0x04, 0x10, 0x27, 0x00, 0x00, 0x18, (byte) 0xf1};
        } else {
            sendData = new byte[]{(byte) 0xC4, 0x13, 0x04, 0x20, 0x4e, 0x00, 0x00, 0x31, (byte) 0xe2};
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
            try {
                byte[] data = new byte[40];
                boolean dataFinish = true;
                int dataLength = 0;
                while (isRun) {
                    byte[] buffer = new byte[40];
                    int size; //读取数据的大小
                    size = getSerialPort().getInputStream().read(buffer, 0, buffer.length);
                    Log.e("rrrr", Arrays.toString(buffer));

                    if (size > 0) {
                        String str123 = new String(buffer, 0, buffer.length).trim();
                        Log.e("rrrr", str123);

                        if (buffer[0] == -60) {
                            System.arraycopy(buffer, 0, data, 0, size);
                            dataLength = size;
                            dataFinish = false;
                        } else {//数据中断区
                            if (!dataFinish) {
                                System.arraycopy(buffer, 0, data, dataLength, 40 - dataLength);
                                dataFinish = true;
                            }
                        }

                        if (dataFinish) {
                            // 数据接收完全可以发数据了
                            if (data[1] == 4 || data[1] == 5) {
                                // 数据完整
                                sendReadMessage(data, WEIGHT_AXE);
//                                MyLog.log("称重数据===" + Arrays.toString(data));
                            }
                        }

//                        String str11 = new String(buffer, 0, buffer.length).trim();
//                        MyLog.log("67890123" + Arrays.toString(buffer));
//                        MyLog.log("67890123 数据的大小size=" + size);
//                        MyLog.log("67890123" + str11);
                    }
                    Thread.sleep(50);
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
}


//-60, 5, 20,
//        -13, -110,
//        2, 32, -45, -86, 0,
//        32,126, 4, 0, 0,
//        0, 0, 0, 0, 1,
//        0, 0, 0, -124, 69,


//-60, 5, 20,
//        58, -69, 2, 32, 8,
//        -80, 2, 32, 26, 0,
//        0, 0, 0, 0, 0,
//        0, 1, 0, 0, 0,
//        -106, 126
