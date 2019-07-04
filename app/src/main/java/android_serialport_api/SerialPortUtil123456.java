//package android_serialport_api;
//
//
//import android.util.Log;
//
//import com.xuanyuan.library.utils.system.LogWriteUtils;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//import company.xs.weightapp.MyApplication;
//import company.xs.weightapp.utils.ChangeTool;
//
//
///**
// * Created by Administrator on 2018/5/31.
// */
//
//public class SerialPortUtil123456 {
//
//    public static String TAG = "XSWeighter15";
//    public static SerialPort serialPort = null;
//    public static InputStream inputStream = null;
//    public static OutputStream outputStream = null;
//    public boolean threadStatus; //线程状态，为了安全终止线程
//    public ChangeTool changeTool = new ChangeTool();
//    public OnDataReceiveListener onDataReceiveListener = null;
//
//
//    /**
//     * 打开串口
//     * @param s
//     * @param i
//     * @param i1
//     */
//    public  boolean open(String s, int i, int i1) {
//        boolean isopen = false;
//        try {
//            serialPort = new SerialPort(new File(s), i, i1);
//
//            inputStream = serialPort.getInputStream();
//            outputStream = serialPort.getOutputStream();
//
//            new ReadThread().start(); //开始线程监控是否有数据要接收
//
//            //new SendThread().start(); //开始线程发送指令
//
//            isopen = true;
//            MyApplication.isFlagSerial = true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            isopen = false;
//        }
//        return isopen;
//    }
//
//    /**
//     * 关闭串口
//     */
//    public static boolean close() {
//        boolean isClose = false;
//        LogWriteUtils.e(TAG, "关闭串口");
//        try {
//            if (inputStream != null) {
//                inputStream.close();
//            }
//            if (outputStream != null) {
//                outputStream.close();
//            }
//            isClose = true;
//            MyApplication.isFlagSerial = false;//关闭串口时，连接状态标记为false
//        } catch (IOException e) {
//            e.printStackTrace();
//            isClose = false;
//        }
//        return isClose;
//    }
//
//    /**
//     * 发送串口指令
//     */
//    public static void sendString(String data) {
//        Log.d(TAG, "sendSerialPort: 发送数据");
//        try {
//            byte[] sendData = data.getBytes(); //string转byte[]
//            if (sendData.length > 0) {
//                outputStream.write(sendData);
//                //outputStream.write('\n');
//                //outputStream.write('\r'+'\n');
//                outputStream.flush();
//                Log.d(TAG, "sendSerialPort: 串口数据发送成功");
//            }
//        } catch (IOException e) {
//            Log.e(TAG, "sendSerialPort: 串口数据发送失败："+e.toString());
//        }
//    }
//
//    public static void sendByteData() {
//        Log.d(TAG, "sendSerialPort: 发送数据");
//        try {
//            //标定10KG砝码
//            byte[] sendData = {0x3C,0x43,0x41,0x4C,0x30,0x30,0x31,0x30, (byte) 0x91,0x3E};
//            if (sendData.length > 0) {
//                outputStream.write(sendData);
//                //outputStream.write('\n');
//                //outputStream.write('\r'+'\n');
//                outputStream.flush();
//                Log.d(TAG, "sendSerialPort: 串口数据发送成功");
//            }
//        } catch (IOException e) {
//            Log.e(TAG, "sendSerialPort: 串口数据发送失败："+e.toString());
//        }
//    }
//
//    /**
//     * 单开一线程，来读数据
//     */
//    //写一个自定义线程来继承自Thread类
//    private class ReadThread extends Thread {
//        @Override
//        public void run() {
//            super.run();
//            //判断进程是否在运行，更安全的结束进程
//            while (!threadStatus){
//                Log.d(TAG, "进入线程run");
//                byte[] buffer = new byte[2048];
//                int size; //读取数据的大小
//                try {
//                    size = inputStream.read(buffer);
//                    if (size > 36){
//                        Log.d(TAG, "run: 接收到了数据：" + changeTool.ByteArrToHex(buffer));
//                        Log.d(TAG, "run: 接收到了数据大小：" + String.valueOf(size));
//                        onDataReceiveListener.onDataReceive(buffer,size);
//                    }
//                    Thread.sleep(150);
//                } catch (IOException e) {
//                    Log.e(TAG, "run: 数据读取异常：" +e.toString());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    /**
//     * 单开一线程，来发数据
//     */
//    //写一个自定义线程来继承自Thread类
//    private class SendThread extends Thread {
//        @Override
//        public void run() {
//            super.run();
//            //判断进程是否在运行，更安全的结束进程
//            while (!threadStatus){
//                Log.d(TAG, "进入线程run");
//                try {
//                    sendString("<WEI >");
//                    Thread.sleep(800);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public  interface OnDataReceiveListener
//    {
//        void onDataReceive(byte[] buffer, int size);
//    }
//
//    public void setOnDataReceiveListener(OnDataReceiveListener dataReceiveListener) {
//        onDataReceiveListener = dataReceiveListener;
//    }
//}