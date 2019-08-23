package com.axecom.smartweight.helper.weighter;

import android.util.Log;

import com.axecom.smartweight.config.IEventBus;
import com.axecom.smartweight.entity.system.BaseBusEvent;
import com.xuanyuan.library.MyLog;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;


/**
 * 香山秤15.6 黑色屏
 */
public class XSWeighter8 extends MyBaseWeighter implements IEventBus {


    public XSWeighter8() {

    }

    public static final String TAG = "XSWeighter15";
    private ReadThread readThread;

    /**
     * 打开串口,数据功能
     */
    public boolean open() {
        int baudrate = 9600;
        String path = "/dev/ttyS1";
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
     * 设置重置按钮
     */
    public void resetBalance() {
        String zer = "<TAR >";
        sendString(zer);
    }

    // 获取K值
    @Override
    public void getKValue() {
        String zer = "<QUE >";
        sendString(zer);
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
            //outputStream.write('\n');
            //outputStream.write('\r'+'\n');
            flush();
        }
    }

    /**
     * private String wei = "<WEI >";  称重指令
     * private String zer = "<ZER >";  置零指令
     * private String tar = "<TAR >";  去皮指令
     * 发送串口指令
     */
    public void sendZer() {
        String zer = "<ZER >";
        byte[] sendData = zer.getBytes(); //string转byte[]
        write(sendData);
        flush();
    }

    @Override
    public void readyBD() {

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
     * 发送标定数据
     */
    public void sendCalibrationData(boolean isCalibration10kg) {
        byte[] sendData;
        if (isCalibration10kg) {
            sendData = new byte[]{0x3C, 0x43, 0x41, 0x4C, 0x30, 0x30, 0x31, 0x30, (byte) 0x91, 0x3E};
        } else {
            sendData = new byte[]{0x3C, 0x43, 0x41, 0x4C, 0x30, 0x30, 0x32, 0x30, (byte) 0x92, 0x3E};
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


        /**
         * 准备状态
         */
        private void readyState(int byte_data) {
            switch (byte_data) {
                case '<':
                    /* *
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
                     */
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
                    break;
            }
        }


        @Override
        public void run() {
            super.run();
            //判断进程是否在运行，更安全的结束进程
//            byte[] buffer = new byte[60];
            try {
                while (isRun) {
                    byte[] buffer = new byte[60];
                    int size; //读取数据的大小
                    size = getSerialPort().getInputStream().read(buffer, 0, buffer.length);
//                    if (size > 2) {
//                        String str11 = new String(buffer, 0, buffer.length).trim();
//                        MyLog.log("获取K值数据=======" + Arrays.toString(buffer));
//                        MyLog.log("获取K值数据=======" + str11);
//
//                        if (str11.endsWith("#")) {
//                            if (str11.startsWith("C1")) {
////                                BaseBusEvent event = new BaseBusEvent();
////                                event.setEventType(BaseBusEvent.WEIGHT_SX15);
////                                event.setOther(str11);
////                                EventBus.getDefault().post(event);
//                                sendReadMessage(str11, BaseBusEvent.WEIGHT_SX8);
//                            } else if (str11.startsWith("C2")) {//C2 001 038# 按键值
//                                sendReadMessage(str11, BaseBusEvent.WEIGHT_KEY_PRESS);
//                            } else if (str11.startsWith("C3") || str11.startsWith("3")) {//C3 1150 1 065#
//                                sendReadMessage(str11, BaseBusEvent.WEIGHT_ELECTRIC);
//                                MyLog.log22("电量值=======" + str11);
//                            } else if (str11.startsWith("C4") || str11.startsWith("4")) {//C4 ERR0  标定
//                                sendReadMessage(str11, BaseBusEvent.WEIGHT_CALIBRATION);
//                            } else if (str11.startsWith("C5") || str11.startsWith("5")) {// 判断是否包含ERR0  ERR1即可
//                                //去皮成功
//
//                            } else if (str11.startsWith("C6") || str11.startsWith("6")) {// 判断是否包含ERR0  ERR1即可
//                                sendReadMessage(str11, BaseBusEvent.WEIGHT_KVALUE);
//                            } else if (str11.startsWith("C7") || str11.startsWith("7")) {//C7 ERR0 009#
//
//                            } else if (str11.startsWith("C8") || str11.startsWith("8")) {//C8 ERR0 009#
//                                //置零成功
//                                sendReadMessage(str11, BaseBusEvent.WEIGHT_ZEROING);
//                            }
//                        }
//                    }


//                    if(size>2){
//                        String str11 = new String(buffer, 0, size).trim();
//                        MyLog.e("tty", "原始数据" + str11);
//                    }

                    if (size >= 46) {// 传输的是称重数据
                        String str = new String(buffer, 0, buffer.length).trim();
//                        MyLog.e("tty", "称重数据" + str);
                        sendReadMessage(str, WEIGHT_SX8);
                    } else if (size < 10 && size >= 5) {//按键数据
                        String str = new String(buffer, 0, size).trim();
                        MyLog.e("tty", "按键" + str);
                        if (str.startsWith("#") && str.endsWith("#")) {
                            sendReadMessage(str, WEIGHT_KEY_PRESS);
                        }
                    } else if (size > 0) {

//                    size = getSerialPortPrinter().getInputStream().read(buffer);
                        String strTest = new String(buffer, 0, size).trim();
//                        MyLog.logTest("称重测试数据===" + strTest);
                    }
                    Thread.sleep(110);
                }
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    /**
     * 发送消息
     *
     * @param str       发送内容
     * @param eventType 发送事件类型
     */
    private void sendReadMessage(String str, String eventType) {
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
     */
    //TODO 待 移动到base类中
    public synchronized int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int c = getSerialPort().getInputStream().read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte) c;
        int i = 1;
        int size = 1;
        //数据是否完了
        boolean isOver = false;
        try {
            for (; i < len; i++) {
                c = getSerialPort().getInputStream().read();
                if (c == -1) {
                    isOver = true;
                }
                if (isOver) {
                    b[off + i] = 0;
                } else {
                    b[off + i] = (byte) c;
                    if (c != 0) {
                        size++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
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

//        AAFE000625BF0006243B00465199000A00053700
//        AAFE0048AB810006243B00465199000A0005C100