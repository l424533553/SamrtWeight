package com.shangtongyin.tools.serialport;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

import com.shangtongyin.tools.test.MyLog;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by zf on 2017/9/2 0002.
 * 商通打印机  USB 打开
 */

public class USB_Print {
    private static final String ACTION_USB_PERMISSION = "com.usb.printer.USB_PERMISSION";

    private static USB_Print mInstance;

    private Context mContext;
    private UsbDevice mUsbDevice;
    private PendingIntent mPermissionIntent;
    private UsbManager mUsbManager;
    private UsbDeviceConnection mUsbDeviceConnection;
    private UsbEndpoint epBulkOut;
    private UsbEndpoint epBulkIn;

    private final BroadcastReceiver mUsbDeviceReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        mUsbDevice = usbDevice;
                        //3)查找设备接口
                        if(getDeviceInterface()) {
                            //4)获取设备endpoint
                            if (Interface1 != null) {
                                assignEndpoint(Interface1);
                                openDevice(Interface1);
                            }
                        }
                    } else {
                        Toast.makeText(context, "Permission denied for device " + usbDevice, Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                if (mUsbDevice != null) {
                    Toast.makeText(context, "Device closed", Toast.LENGTH_SHORT).show();
                    if (mUsbDeviceConnection != null) {
                        mUsbDeviceConnection.close();
                    }
                }
            }
        }
    };

    private USB_Print() {

    }

    public static USB_Print getInstance() {
        if (mInstance == null) {
            mInstance = new USB_Print();
        }
        return mInstance;
    }

    /**
     * 初始化打印机，需要与destroy对应
     *
     * @param context 上下文
     */
    public static void initPrinter(Context context) {
        getInstance().init(context);
    }

    /**
     * 销毁打印机持有的对象
     */
    public static void destroyPrinter() {
        getInstance().destroy();
    }

    private void init(Context context) {
        mContext = context;
        mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        mContext.registerReceiver(mUsbDeviceReceiver, filter);

        // 列出所有的USB设备，并且都请求获取USB权限
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        int count = deviceList.size();
        if(count > 0) {
            while (deviceIterator.hasNext()) {
                UsbDevice device = deviceIterator.next();
                MyLog.e("device.getDeviceId()",device.getDeviceId()+"");
                String devicename = device.getDeviceName();
                if(checkUsbDevicePidVid(device)){
                    Log.e("devicename",devicename);
                    mUsbDevice=device;
                    mUsbManager.requestPermission(device, mPermissionIntent);
                }
            }
        }
//        for (UsbDevice device : deviceList.values()) {
//            if(checkUsbDevicePidVid(device))
//                mUsbManager.requestPermission(device, mPermissionIntent);
//        }

    }
    boolean checkUsbDevicePidVid(UsbDevice dev) {
        int pid = dev.getProductId();
        int vid = dev.getVendorId();
        MyLog.e("pid",pid+"");
        MyLog.e("vid",vid+"");
        boolean rel = false;
        if ((vid == 4070 && pid == 33054)||(vid == 1659 && pid == 8963) ) {
            rel = true;
        }
        return rel;
    }
    private void destroy() {
        mContext.unregisterReceiver(mUsbDeviceReceiver);
        if (mUsbDeviceConnection != null) {
            mUsbDeviceConnection.close();
            mUsbDeviceConnection = null;
        }

        mContext = null;
        mUsbManager = null;
    }


    // 寻找设备接口
    private UsbInterface Interface1,Interface2;
    private boolean getDeviceInterface() {
        if (mUsbDevice != null) {
            MyLog.e("1",mUsbDevice.getInterfaceCount()+"");
            for (int i = 0; i < mUsbDevice.getInterfaceCount(); i++) {
                UsbInterface intf = mUsbDevice.getInterface(i);
                MyLog.e("intf",intf.getInterfaceClass()+"");
                if (i == 0) {
                    Interface1 = intf; // 保存设备接口
                    Log.e("成功获得设备接口1:","" + Interface1.getId());
                }
                if (i == 1) {
                    Interface2 = intf;
                    Log.e("成功获得设备接口2:","" + Interface2.getId());
                }
            }
            return true;
        } else {
            return false;
        }
    }
    /**
     * 控制端点
     */
    private UsbEndpoint epControl;
    /**
     * 中断端点
     */
    private UsbEndpoint epIntEndpointOut;
    private UsbEndpoint epIntEndpointIn;
    private UsbEndpoint assignEndpoint(UsbInterface mInterface) {

        for (int i = 0; i < mInterface.getEndpointCount(); i++) {
            UsbEndpoint ep = mInterface.getEndpoint(i);
            // look for bulk endpoint
            if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                    epBulkOut = ep;
                } else {
                    epBulkIn = ep;
                }
            }
            // look for contorl endpoint
            if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_CONTROL) {
                epControl = ep;
            }
            // look for interrupte endpoint
            if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_INT) {
                if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                    epIntEndpointOut = ep;
                }
                if (ep.getDirection() == UsbConstants.USB_DIR_IN) {
                    epIntEndpointIn = ep;
                }
            }
        }
        if (epBulkOut == null && epBulkIn == null && epControl == null
                && epIntEndpointOut == null && epIntEndpointIn == null) {
            throw new IllegalArgumentException("not endpoint is founded!");
        }
        return epIntEndpointIn;
    }
    // 打开设备
    public void openDevice(UsbInterface mInterface) {
        MyLog.e("tag","conn1");
        if (mInterface != null) {
            MyLog.e("tag","conn2");
            UsbDeviceConnection conn = null;
            // 在open前判断是否有连接权限；对于连接权限可以静态分配，也可以动态分配权限

            if (mUsbManager.hasPermission(mUsbDevice)) {
                conn = mUsbManager.openDevice(mUsbDevice);
            }
            if (conn == null) {
                return;
            }
            MyLog.e("tag","conn3");
            if (conn.claimInterface(mInterface, true)) {
                mUsbDeviceConnection = conn;
                if (mUsbDeviceConnection != null)//
                    Log.e("open", "设备成功！");
            } else {
                conn.close();
            }
        }
    }
    // 发送数据
    public int sendMessageToPoint(byte[] buffer) {
        if(mUsbDeviceConnection==null||epBulkOut==null) {
            return -2;
        }else{
            int size = mUsbDeviceConnection.bulkTransfer(epBulkOut, buffer, buffer.length, 0);
            MyLog.e("s","ss"+size);
            if (size < 0) {
                return -1;
            }else{
                return size;
            }
        }
    }
    //接收数据
    private int receiveMessageFromPoint() {
        byte[] buffer = new byte[64];
        int size=mUsbDeviceConnection.bulkTransfer(epBulkIn, buffer, buffer.length, 2000);
        if (size < 0) {
            //没有
            return -2;
        } else {
            String str = new String(buffer, 0, size).trim();
            return 1;
        }
    }
}
