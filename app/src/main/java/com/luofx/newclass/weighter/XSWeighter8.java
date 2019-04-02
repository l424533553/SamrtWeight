package com.luofx.newclass.weighter;

import android.os.Handler;
import android.util.Log;

import com.axecom.smartweight.my.entity.BaseBusEvent;
import com.xuanyuan.library.MyLog;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Arrays;

/**
 * 香山秤15.6 黑色屏
 */
public class XSWeighter8 extends MyBaseWeighter {
    private static XSWeighter8 mInstance;

    public static XSWeighter8 getXSWeighter() {
        return mInstance;
    }

    public XSWeighter8() {
        mInstance = this;
    }

    public static String TAG = "XSWeighter15";
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
        String zer = "<ZER >";
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

    /**
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
    private int rc_state = 0;// 状态
    private int rc_step = 0;
    private int BatVol = 0;
    private int BatChagerFlag = 0;
    private int ErrorFlag = 0;
    private int CurrentAD = 0;
    private int zeroAd = 0;
    private int currentWeight = 0;
    private int tareWeight = 0;
    private int dataFlag = 0;
    private int KeyboardVal = 0;

    private int CalibrationAD = 0;   //砝码AD
    private int CalibrationZero = 0;    //标定零位
    private double K_value = 0.0;

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
                    rc_state = 1;   //返回的数据，包括电池，和命令返回状态
                    break;
                case '#':
                    rc_state = 6;   //按钮值
                    break;
                case 10:            //可能是称重数据
                    rc_step = 1;
                    break;
                case 13:            //称重数据
                    if (rc_step == 1) {      //当前AD
                        rc_state = 9;       //处理称重数据
                        CurrentAD = 0;      //当前AD置零
                    }
                    break;
                default:        //当做标定参数
                    if (byte_data >= '0' && byte_data <= '9') {
                        rc_step = 1;
                        rc_state = 15;
                        CalibrationAD = byte_data - '0';
                    }
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
                    size = getSerialPort().getInputStream().read(buffer, 0, 60);
                    MyLog.blue("data数据：" + Arrays.toString(buffer));

                    for (int i = 0; i < size; i++) {
                        int byte_data = buffer[i];
                        switch (rc_state) {
                            case 0: //处于空闲状态
                                readyState(byte_data);
                                break;
                            case 15:
                                switch (rc_step) {
                                    case 1:         //标定AD
                                        if (byte_data >= '0' && byte_data <= '9') {
                                            CalibrationAD *= 10;
                                            CalibrationAD += byte_data - '0';
                                        } else if (byte_data == ' ') {
                                            rc_step = 2;
                                            CalibrationZero = 0;
                                        } else       //出错了
                                        {
                                            rc_state = 0;
                                        }
                                        break;
                                    case 2:            //标定零位
                                        if (byte_data >= '0' && byte_data <= '9') {
                                            CalibrationZero *= 10;
                                            CalibrationZero += byte_data - '0';
                                        } else if (byte_data == ' ') {
                                            rc_step = 3;
                                            K_value = 0.0;
                                        } else       //出错了
                                        {
                                            rc_state = 0;
                                        }
                                        break;
                                    case 3:         //K值整数位
                                        if (byte_data >= '0' && byte_data <= '9') {
                                            K_value *= 10;
                                            K_value += byte_data - '0';
                                        } else if (byte_data == '.') {
                                            rc_step = 4;
                                        } else //出错了
                                        {
                                            rc_state = 0;
                                        }
                                        break;
                                    default:
                                        if (byte_data >= '0' && byte_data <= '9') {
                                            K_value *= 10;
                                            K_value += byte_data - '0';
                                            rc_step++;
                                            if (rc_step == 11) {
                                                K_value /= 10000000;
                                                rc_step = 0;
                                                rc_state = 0;
                                                MyLog.blue("标定参数：标定AD=" + CalibrationAD + "  标定零位=" + CalibrationZero + " K值=" + K_value);
                                            }
                                        }
                                        break;
                                }
                                break;
                            case 9:
                                if (byte_data >= '0' && byte_data <= '9') {
                                    switch (rc_step) {
                                        case 1:     //获取当前AD值
                                            CurrentAD *= 10;
                                            CurrentAD += byte_data - '0';
                                            break;
                                        case 2:     //获取零位AD
                                            zeroAd *= 10;
                                            zeroAd += byte_data - '0';
                                            break;
                                        case 3:     //获取净重值
                                            currentWeight *= 10;
                                            currentWeight += byte_data - '0';
                                            break;
                                        case 4:     //获取皮重
                                            tareWeight *= 10;
                                            tareWeight += byte_data - '0';
                                            break;
                                        case 5:     //防作弊标志
                                        case 6:     //净重是否为负
                                        case 7:     //是否超载
                                        case 8:     //是否零位
                                        case 9:     //是否去皮
                                        case 10:    //是否稳定
                                            if (byte_data == '0' || byte_data == '1') {
                                                dataFlag <<= 1;
                                                dataFlag |= (byte_data - '0');
                                            } else {
                                                rc_step = 0;
                                            }
                                            if (rc_step == 10) {     //去掉不明字节数据
                                                rc_state = 10;
                                            }
                                            break;
                                    }
                                } else if (byte_data == ' ') {
                                    switch (rc_step) {
                                        case 1:
                                            zeroAd = 0;
                                            break;
                                        case 2:
                                            currentWeight = 0;
                                            break;
                                        case 3:
                                            tareWeight = 0;
                                            break;
                                        case 4:
                                            dataFlag = 0;
                                            break;
                                    }
                                    rc_step += 1;
                                }
                                break;
                            case 10:            //重量数据接收完成
                                /* 在这里添加称重数据接收后的处理 */
                                //TODO  数据处理完成
                                MyLog.blue("称重数据:当前AD=" + CurrentAD
                                        + " 零位AD=" + zeroAd
                                        + "  净量=" + currentWeight
                                        + " 皮重=" + tareWeight
                                        + " 标识=" + dataFlag);

                                BaseBusEvent event = new BaseBusEvent();
                                event.setEventType(BaseBusEvent.NOTIFY_WEIGHT_SX15);
                                WeightBeanXS weightBeanXS = new WeightBeanXS();
                                weightBeanXS.setCurrentAD(CurrentAD);
                                weightBeanXS.setZeroAd(zeroAd);
                                weightBeanXS.setCurrentWeight(currentWeight);
                                weightBeanXS.setTareWeight(tareWeight);
                                weightBeanXS.setDataFlag(dataFlag);
                                event.setOther(weightBeanXS);
                                EventBus.getDefault().post(event);
                                rc_state = 0;
                                break;
                            case 1:
                                if (byte_data == 'E') {  //判断是否为命令返回状态
                                    ErrorFlag = byte_data;
                                    rc_state = 2;
                                } else       //电池电压和充电状态
                                {
                                    rc_state = 3;
                                    if (byte_data >= '0' && byte_data <= '9') {
                                        BatVol = byte_data - '0';
                                    }
                                }
                                break;
                            case 2://ERR？
                                if (byte_data == ' ') {
                                    rc_state = 5;
                                    if (ErrorFlag == 0x45525230 || ErrorFlag == 0x45525231) {
                                        MyLog.blue("返回结果" + ErrorFlag);

                                    }
                                } else if (byte_data == 'J' || byte_data == 'j') {
//                                  1163022897  置零成功
                                    rc_state = 5;
                                    MyLog.bluelog("返回结果" + ErrorFlag);
                                } else if (byte_data == 'R' || byte_data == '0' || byte_data == '1') {
                                    ErrorFlag <<= 8;
                                    ErrorFlag |= byte_data;
                                } else    //出错了
                                {
                                    rc_state = 0;
                                }
                                break;
                            case 3:
                                if (byte_data >= '0' && byte_data <= '9') {  //电池充电标志
                                    BatVol *= 10;
                                    BatVol += byte_data - '0';
                                } else if (byte_data == ' ') {
                                    rc_state = 4;
                                } else //出错了
                                {
                                    rc_state = 0;
                                }
                                break;
                            case 4:
                                if (byte_data >= '0' && byte_data <= '9') {  //电池充电标志
                                    BatChagerFlag = byte_data - '0';
                                } else if (byte_data == ' ') { //出错了
                                    rc_state = 5;
                                } else { //出错了
                                    rc_state = 0;
                                }
                                break;
                            case 5:
                                if (byte_data == '>') {
                                    MyLog.blue("电池数据：电压=" + BatVol + " 充电=" + BatChagerFlag);
                                    MyLog.blue("命令状态：状态=" + ErrorFlag);
                                } else       //出错了
                                {

                                }
                                rc_state = 0;
                                break;
                            case 6:
                                if (byte_data != '#') {
                                    if (byte_data >= '0' && byte_data <= '9') {
                                        KeyboardVal = byte_data - '0';
                                        rc_state = 7;
                                    } else      //出错了
                                    {
                                        rc_state = 0;
                                    }
                                }
                                break;
                            case 7:
                                if (byte_data >= '0' && byte_data <= '9') {
                                    KeyboardVal = byte_data - '0';
                                } else if (byte_data == '#') {
                                    rc_state = 8;
                                } else //出错了
                                {
                                    rc_state = 0;
                                }
                                break;
                            case 8:
                                if (byte_data == '#') {
                                /*
                                在这里添加按键值接收后的处理
                                 */
                                    MyLog.blue("按键状态：按键值=" + KeyboardVal);
                                }
                                rc_state = 0;
                                break;
                            default:
                                rc_state = 0;
                                break;
                        }
                    }



/*
                    MyLog.blue("数据+电池="+BatVol +"  充电="+BatChagerFlag
                            +"   命令="+ErrorFlag+"   当前AD="+ CurrentAD+" 零位AD="+zeroAd
                            +"  标定AD="+CalibrationAD+"  标定零位="+CalibrationZero+" K值="+K_value
                            +"  净量="+ currentWeight+" 皮重="+tareWeight+" 标识="+dataFlag +" 按键值="+KeyboardVal);
*/


              /*      //TODO 说明
                    if (size >= 46) {// 传输的是称重数据
                        String str = new String(buffer, 0, buffer.length).trim();
                        MyLog.logTest("待处理的称重数据" + str);
                        //传输的数据是数据加 空数据间隔 传送
                        if (!TextUtils.isEmpty(str)) {
                            if (handler != null) {
                                Message msg = handler.obtainMessage(WEIGHT_NOTIFY_XS8, str);
                                handler.sendMessage(msg);
                            }
//                                BaseBusEvent event = new BaseBusEvent();
//                                event.setEventType(WEIGHT_DATA_XS8);
//                                event.setOther(str);
//                                EventBus.getDefault().post(event);
                        }
                    } else if (size > 20 && size < 30) {
                        String str = new String(buffer, 0, buffer.length).trim();
                        BaseBusEvent event = new BaseBusEvent();
                        event.setEventType(BaseBusEvent.TYPE_GET_K_VALUE);
                        event.setOther(str);
                        EventBus.getDefault().post(event);
                    }

                    // 数据功能
//                    if (size > 4) {
//                        String str = new String(buffer, 0, size).trim();
//                        MyLog.logTest("data数据："+ Arrays.toString(buffer));
//                        MyLog.logTest("data数据String："+ str);
//                        //传输的数据是数据加 空数据间隔 传送
//                        if (!TextUtils.isEmpty(str)) {
////                          MyLog.myInfo("收到的数据" + str);
//                            if (handler != null) {
////                                Message msg = handler.obtainMessage(WEIGHT_NOTIFY_XS15, str);
////                                handler.sendMessage(msg);
//                            }
//                        }
//                    }*/


                    Thread.sleep(150);
                }
            } catch (IOException e) {
                Log.e(TAG, "run: 数据读取异常：" + e.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
    public synchronized int read(byte b[], int off, int len) throws IOException {
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

