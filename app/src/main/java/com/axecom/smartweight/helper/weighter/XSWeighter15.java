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
public class XSWeighter15 extends MyBaseWeighter implements IEventBus {
    private static XSWeighter15 mInstance;

    public XSWeighter15() {

    }

    public static final String TAG = "XSWeighter15";
    private ReadThread readThread;

    /**
     * 打开串口,数据功能
     */
    public boolean open() {
        int baudrate = 19200;
        String path = "/dev/ttyS4";
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
        //TODO
//        String zer = "<ZER >";
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
    @Override
    public void sendZer() {
        String zer = "<ZER >";
        byte[] sendData = zer.getBytes(); //string转byte[]
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
            //outputStream.write('\n');
            //outputStream.write('\r'+'\n');
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
     */
    @Override
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

        @Override
        public void run() {
            super.run();
            //判断进程是否在运行，更安全的结束进程
//            byte[] buffer = new byte[60];
            try {

                if (getSerialPort() == null) {
                    return;
                }
                while (isRun) {
                    byte[] buffer = new byte[50];
                    int size; //读取数据的大小
                    size = getSerialPort().getInputStream().read(buffer, 0, buffer.length);
                    if (size > 2) {
                        String str11 = new String(buffer, 0, buffer.length).trim();
//                        MyLog.log("获取K值数据=======" + Arrays.toString(buffer));
//                        MyLog.log("获取K值数据=======" + str11);

                        if (str11.endsWith("#")) {
                            if (str11.startsWith("C1")) {
                                MyLog.log("获取重量数据=======" + str11);
                                String[] array = str11.split(" ");
                                if (array.length >= 6 && array[1].length() == 7
                                        && array[2].length() == 7 && array[3].length() == 7
                                        && array[4].length() == 7 && array[5].length() == 7) {
                                    sendReadMessage(array, WEIGHT_SX15);
                                }
                            } else if (str11.startsWith("C2")) {//C2 001 038# 按键值
                                sendReadMessage(str11, WEIGHT_KEY_PRESS);
                            } else if (str11.startsWith("C3") || str11.startsWith("3")) {//C3 1150 1 065#  电池内容
                                String[] array = str11.split(" ");
                                if (array.length >= 3 && array[1].length() == 4 && array[2].length() == 2) {
                                    sendReadMessage(array, WEIGHT_ELECTRIC);
                                }
                                MyLog.log22("电量值=======" + str11);
                            } else if (str11.startsWith("C4") || str11.startsWith("4")) {//C4 ERR0  标定
                                sendReadMessage(str11, WEIGHT_CALIBRATION);
                            } else if (str11.startsWith("C5") || str11.startsWith("5")) {// 判断是否包含ERR0  ERR1即可
                                //去皮成功
                            } else if (str11.startsWith("C6") || str11.startsWith("6")) {// 判断是否包含ERR0  ERR1即可
                                MyLog.log22("获取K标定值=======" + str11);
                                sendReadMessage(str11, WEIGHT_KVALUE);
                            } else if (str11.startsWith("C7") || str11.startsWith("7")) {//C7 ERR0 009#
                                sendReadMessage(str11, WEIGHT_CLEAN_CHEAT);
                            } else if (str11.startsWith("C8") || str11.startsWith("8")) {//C8 ERR0 009#
                                //置零成功
                                sendReadMessage(str11, WEIGHT_ZEROING);
                            }
                        }
                    }

                    Thread.sleep(150);
                }
            } catch (IOException e) {
                Log.e(TAG, "run: 数据读取异常：" + e.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendReadMessage(Object str, String eventType) {
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
        if (getSerialPort() == null) {
            return -1;
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


//67, 49, 32, 50, 49, 49, 56, 56, 51, 48,
//        32, 50, 49, 48, 54, 57, 48, 53, 32, 48,
//        48, 48, 48, 52, 53, 53, 32, 48, 48, 48,
//        48, 48, 48, 48, 32, 49, 48, 48, 48, 48,
//        49, 35, 0, 0,
