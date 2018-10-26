//package com.axecom.iweight.manager;
//
//import android.content.Context;
//import android.hardware.usb.UsbDevice;
//import android.hardware.usb.UsbDeviceConnection;
//import android.hardware.usb.UsbManager;
//import android.util.Log;
//
//import com.axecom.iweight.base.BusEvent;
//import com.axecom.iweight.base.SysApplication;
//import com.axecom.iweight.utils.LogUtils;
//
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class TemperatureUsbControl {
//    private static final String TAG = TemperatureUsbControl.class.getSimpleName();
//    private static final String TEMPERATURE_USB_VENDOR_ID = "1A86";     //供应商id
//    private static final String TEMPERATURE_USB_PRODUCT_ID = "7523";    //产品id
//    private static final String GPRINTER_USB_VENDOR_ID = "6868";     //供应商id
//    private static final String GPRINTER_USB_PRODUCT_ID = "500";    //产品id
//    private Context mContext;
//    private UsbManager mUsbManager; //USB管理器
//    private UsbSerialPort sTemperatureUsbPort = null;  //接体温枪的usb端口
//    private SerialInputOutputManager mSerialIoManager;  //输入输出管理器（本质是一个Runnable）
//    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();  //用于不断从端口读取数据
//    //数据输入输出监听器
//    private final SerialInputOutputManager.Listener mListener =
//            new SerialInputOutputManager.Listener() {
//
//                @Override
//                public void onRunError(Exception e) {
//                    Log.d(TAG, "Runner stopped.");
//                }
//
//                @Override
//                public void onNewData(final byte[] data) {
//                    Log.d(TAG, "new data.");
//                    EventBus.getDefault().post(new BusEvent(BusEvent.USB_NEW_DATA, data));
//                }
//            };
//
//    public TemperatureUsbControl(Context context) {
//        mContext = context;
//    }
//
//    public void initUsbControl() {
//        mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
//        //全部设备
//        List<UsbSerialDriver> usbSerialDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);
//        //全部端口
//        List<UsbSerialPort> usbSerialPorts = new ArrayList<UsbSerialPort>();
//        for (UsbSerialDriver driver : usbSerialDrivers) {
//            List<UsbSerialPort> ports = driver.getPorts();
//            Log.d(TAG, String.format("+ %s: %s port%s",
//                    driver, Integer.valueOf(ports.size()), ports.size() == 1 ? "" : "s"));
//            usbSerialPorts.addAll(ports);
//        }
//        String vendorId;
//        String productId;
//        //校验设备，设备是 2303设备
//        for (UsbSerialPort port : usbSerialPorts) {
//            UsbSerialDriver driver = port.getDriver();
//            UsbDevice device = driver.getDevice();
//            vendorId = HexDump.toHexString((short) device.getVendorId());
//            productId = HexDump.toHexString((short) device.getProductId());
//            if (vendorId.equals(TEMPERATURE_USB_VENDOR_ID) && productId.equals(TEMPERATURE_USB_PRODUCT_ID)) {
//                sTemperatureUsbPort = port;
//            }
//            if (vendorId.equals(GPRINTER_USB_VENDOR_ID) && productId.equals(GPRINTER_USB_PRODUCT_ID)) {
//                SysApplication.getInstances().setGpDriver(driver);
//            }
//        }
//        if (sTemperatureUsbPort != null) {
//            //成功获取端口，打开连接
//            UsbDeviceConnection connection = mUsbManager.openDevice(sTemperatureUsbPort.getDriver().getDevice());
//            if (connection == null) {
//                Log.e(TAG, "Opening device failed");
//                return;
//            }
//            try {
//                sTemperatureUsbPort.open(connection);
//                //设置波特率
//                sTemperatureUsbPort.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
//
//            } catch (IOException e) {
//                //打开端口失败，关闭！
//                Log.e(TAG, "Error setting up device: " + e.getMessage(), e);
//                try {
//                    sTemperatureUsbPort.close();
//                } catch (IOException e2) {
//                    // Ignore.
//                }
//                sTemperatureUsbPort = null;
//                return;
//            }
//        } else {
//            //提示未检测到设备
//        }
//    }
//
//
//    public void onDeviceStateChange() {
//        //重新开启USB管理器
//        stopIoManager();
//        startIoManager();
//    }
//
//    private void startIoManager() {
//        if (sTemperatureUsbPort != null) {
//            Log.i(TAG, "Starting io manager ..");
////            mSerialIoManager = new SerialInputOutputManager(sTemperatureUsbPort, mListener);
////            mExecutor.submit(mSerialIoManager);  //实质是用一个线程不断读取USB端口
//
//            new ReadThread(sTemperatureUsbPort).run();
//        }
//    }
//    public boolean threadStatus = false; //线程状态，为了安全终止线程
//
//    private class ReadThread extends Thread {
//        UsbSerialPort port;
//        public ReadThread(UsbSerialPort port ){
//            this.port = port;
//        }
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
//                    String s = "";
//                    String s1 = "";
//                    for (byte b : buffer) {
//                        String s2 = String.format("%02x ", b).substring(0, 2);
//                        s1 += Integer.parseInt(String.format("%02x ", b).substring(0, 2), 16);
//                        s += String.format("%02x ", b);
//
//                    }
//                    LogUtils.d("Read " + s1 + " bytes.");
//
//                } catch (IOException e) {
//                    LogUtils.e("run: 数据读取异常：" + e.toString());
//                }
//            }
//
//        }
//    }
//
//    private void stopIoManager() {
//        if (mSerialIoManager != null) {
//            Log.i(TAG, "Stopping io manager ..");
//            mSerialIoManager.stop();
//            mSerialIoManager = null;
//        }
//    }
//
//    public void onPause() {
//        stopIoManager();
//        if (sTemperatureUsbPort != null) {
//            try {
//                sTemperatureUsbPort.close();
//            } catch (IOException e) {
//                // Ignore.
//            }
//            sTemperatureUsbPort = null;
//        }
//    }
//}
//
