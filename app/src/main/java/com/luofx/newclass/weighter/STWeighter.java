package com.luofx.newclass.weighter;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.axecom.smartweight.my.config.IConstants;
import com.axecom.smartweight.my.entity.BaseBusEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * luofaxin 20180829 修改
 * 商通SY2000 称重模块 重量读取帮助类
 */
public class STWeighter extends MyBaseWeighter implements IConstants {

    /**
     * 读取重量的命令
     */
    private byte[] readWeightOrder = new byte[]{0x55, (byte) 0xAA};


//    private byte[] readWeightAD = new byte[]{0x55, (byte) 0xAE};
    // 0x55 0xaa是重量值   0x55 0xae ad值    0x55 0xad
    //0x55 0x00 置零
    //0x55 0x0F  去皮
    //0x55 0xAD  读取AD值

    private ReadThread mThread;
    private GetWeightThreadForST gThread;


    /**
     * @return 开启式否成功
     */
    public boolean open() {
        int baudrate = 9600;
        String path = "/dev/ttyS0";
        if (!super.open(path, baudrate)) {
            return false;
        }
        mThread = new ReadThread();
        mThread.start();
        gThread = new GetWeightThreadForST();
        gThread.start();
        return true;

    }


//    /**
//     * 获得打印机的串口 控制
//     *
//     * @return 打印机串口
//     */
//    public SerialPort getSerialPort() throws SecurityException,
//            IOException, InvalidParameterException {
//        if (serialPort == null) {
//            serialPort = new SerialPort(new File("/dev/ttyS0"), 9600, 0);
//        }
//        return serialPort;
//    }



    //关闭
    public void closeSerialPort() {
        if (gThread != null) {
            gThread.stopRun();
        }
        if (mThread != null) {
            mThread.stopRun();
        }
        super.closeSerialPort();
    }


    public class GetWeightThreadForST extends Thread {
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
            while (isRun) {
                try {
                    getSerialPort().getOutputStream().write(readWeightOrder);
                    int TIMERDELAY = 200;
                    sleep(TIMERDELAY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 重量数据读取线程
     */
    private class ReadThread extends Thread {
        private boolean isRun;
        byte[] buffer = new byte[64];
        private int size;

        private ReadThread() {
            this.isRun = true;
        }

        private void stopRun() {
            this.isRun = false;
            interrupt();
        }

        @Override
        public void run() {
            super.run();
            while (isRun) {
                //发出读取重量的命令   getSerialPort().getOutputStream().write(readWeightOrder);
                //休眠100ms, 然后读取重量信息数据 ,此方法不可靠
                try {
                    if (getSerialPort() != null) {
                        size = getSerialPort().getInputStream().read(buffer);
                        if (size > 0) {
                            String str = new String(buffer, 0, size).trim();
                            //传输的数据是数据加 空数据间隔 传送
                            if (!TextUtils.isEmpty(str)) {

                                if (str.contains("kg")) {
                                    int index = str.indexOf("kg");
                                    str = str.substring(0, index).trim().replace(" ", "");
                                    if (!TextUtils.isEmpty(str)) {
//                                        MyLog.myInfo("收到的数据" + str);
                                        BaseBusEvent event=new BaseBusEvent();
                                        int WEIGHT_NOTIFY_ST = 2013;//商通的称重通知
                                        event.setEventType(BaseBusEvent.NOTIFY_WEIGHT_ST);
                                        event.setOther(str);
                                        EventBus.getDefault().post(event);
                                    }
                                }
                            }
                        }
                    }
                    int READDELAY_50 = 50;
                    Thread.sleep(READDELAY_50);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 称重模块置零 命令
     */
    public void resetBalance() {
        byte[] order = new byte[]{0x55, 0x00};
        write(order);
    }

    @Override
    public void getKValue() {

    }


    public void suspend() {
        if (gThread != null) {
            gThread.stopRun();
            gThread.interrupt();
            gThread = null;
        }
    }

    public void restart() {
        gThread = new GetWeightThreadForST();
        gThread.start();
    }
}
