//package com.axecom.iweight.manager;
//
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.hardware.usb.UsbDeviceConnection;
//import android.hardware.usb.UsbManager;
//
//import com.axecom.iweight.base.SysApplication;
//import com.axecom.iweight.ui.activity.HomeActivity;
//import com.axecom.iweight.utils.LogUtils;
//import com.hoho.android.usbserial.driver.UsbSerialDriver;
//import com.hoho.android.usbserial.driver.UsbSerialPort;
//import com.hoho.android.usbserial.driver.UsbSerialProber;
//
//import java.io.IOException;
//import java.util.List;
//
///**
// * Created by Administrator on 2018/7/8.
// */
//
//public class UsbHostManager {
//
//    public boolean threadStatus = false; //线程状态，为了安全终止线程
//    private UsbSerialPort port;
//    private UsbManager manager;
//    private OnCallbackListener onCallbackListener;
//    private Context context;
//
//    public UsbHostManager(Context context){
//        manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
//        this.context = context;
//    }
//
//    public interface OnCallbackListener{
//        void onCallback(String result);
//    }
//
//    public void usbOpen(OnCallbackListener onCallbackListener) {
//        threadStatus = false;
//        this.onCallbackListener = onCallbackListener;
//        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
//        if (availableDrivers.isEmpty()) {
//            return;
//        }
//
//        for (int i = 0; i < availableDrivers.size(); i++) {
//            UsbSerialDriver driver = availableDrivers.get(i);
//            if (driver.getDevice().getVendorId() == 6790 && driver.getDevice().getProductId() == 29987) {
//                SysApplication.getInstances().setCardDevice(driver.getDevice());
//                PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent("com.android.example.USB_PERMISSION"), 0);
//                UsbDeviceConnection connection = null;
//
//                if (manager.hasPermission(driver.getDevice())) {
//                    //if has already got permission, just goto connect it
//                    //that means: user has choose yes for your previously popup window asking for grant perssion for this usb device
//                    //and also choose option: not ask again
//                    connection = manager.openDevice(driver.getDevice());
//                } else {
//                    //this line will let android popup window, ask user whether to allow this app to have permission to operate this usb device
//                    manager.requestPermission(driver.getDevice(), mPermissionIntent);
//                }
//
//                if (connection == null) {
//                    // You probably need to call UsbManager.requestPermission(driver.getDevice(), ..)
//                    return;
//                }
//                port = driver.getPorts().get(0);
//                try {
//                    port.open(connection);
//                    port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
//                    new ReadThread().start();
//                } catch (IOException e) {
//                    // Deal with error.
//                } finally {
//                }
//            }
//            if (driver.getDevice().getVendorId() == 26728 && driver.getDevice().getProductId() == 1280) {
//                SysApplication.getInstances().setGpDriver(driver);
//            }
//        }
//
//        if (SysApplication.getInstances().getCardDevice() == null) {
////            showLoading("没有插入读卡器，请检查设备");
//        }
////        if (SysApplication.getInstances().getGpDriver() == null) {
////            showLoading("没有插入打印机，请检查设备");
////        }
//// Read some data! Most have just one port (port 0).
//
//    }
//
//    /**
//     * 单开一线程，来读数据
//     * <p>
//     * 55 aa 14 16 ff 05 d6 29 95 a2 c8 08 04 00 01 62 b9 9d b5 0f b8 1d 00 ff 00 00 00 00 00 00 00 00
//     * 55 aa 14 16 ff 05 a6 45 9e e2 9f 08 04 00 01 be d6 7a 56 ab 67 1d 00 ff 00 00 00 00 00 00 0
//     * 55 aa 14 16 ff 05 06 e9 93 a2 de 08 04 00 01 82 60 7a 68 ec 9f 1d 00 ff 00 00 00 00 00 00 00 00  bytes.
//     * 55 aa 14 16 ff 05 d3 12 da 2d 36 08 04 00 01 6a a7 b0 d8 5e 50 1d 00 ff 00 00 00 00 00 00 00 00  bytes.
//     */
//    private class ReadThread extends Thread {
//
//        @Override
//        public void run() {
//            super.run();
//            //判断进程是否在运行，更安全的结束进程
//            while (!threadStatus) {
//                LogUtils.d("进入线程run");
//                //64   1024
//                byte[] buffer = new byte[32];
//                int size; //读取数据的大小
//                try {
//                    int numBytesRead = port.read(buffer, 1000);
//                    if (numBytesRead > 0) {
//                        String s = "";
//                        for (byte b : buffer) {
//                            s += String.format("%02x ", b);
//                        }
//                        LogUtils.d("Read " + s + " bytes.");
//                        String[] cards = s.split(" ");
//                        String cardNo="";
//                        for (int i = 9; i > 5; i--) {
//                            cardNo+=cards[i];
//                        }
//                        onCallbackListener.onCallback(cardNo);
//                        final String finalCardNo = cardNo;
////                        runOnUiThread(new Runnable() {
////                            @Override
////                            public void run() {
////                                cardNumberTv.setText(finalCardNo.toUpperCase());
////                            }
////                        });
//                    }
//
//                } catch (IOException e) {
//                    LogUtils.e("run: 数据读取异常：" + e.toString());
//                }
//            }
//
//        }
//    }
//
//    public void stopRead(){
//        threadStatus = true;
//    }
//}
