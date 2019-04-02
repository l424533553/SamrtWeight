package com.luofx.newclass;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by zf on 2017/5/31.
 */

public class USBService extends Service {
    private MyBinder mBinder = new MyBinder();
    private UsbManager manager ;
    private UsbInterface Interface1,Interface2;
    /**
     * 块输出端点
     */
    private UsbEndpoint epBulkOut;
    private UsbEndpoint epBulkIn;
    /**
     * 连接
     */
    private UsbDeviceConnection myDeviceConnection;
    /**
     * 满足的设备
     */
    private UsbDevice myUsbDevice;
    private PendingIntent pendingIntent;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    /**
     * 枚举设备
     */
    private void enumeraterDevices(){
        getUsbDeviceList();
    }
    boolean checkUsbDevicePidVid(UsbDevice dev) {
        int pid = dev.getProductId();
        int vid = dev.getVendorId();
        boolean rel = false;
        if ((vid == 34918 && pid == 256) || (vid == 1137 && pid == 85)
                || (vid == 6790 && pid == 30084)
                || (vid == 26728 && pid == 256) || (vid == 26728 && pid == 512)
                || (vid == 26728 && pid == 256) || (vid == 26728 && pid == 768)
                || (vid == 26728 && pid == 1024)|| (vid == 26728 && pid == 1280)
                || (vid == 26728 && pid == 1536)) {
            return true;
        }
        if(vid == 1155){
            rel = true;
        }
        return rel;
    }

    public void getUsbDeviceList() {
        // Get the list of attached devices
        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        myUsbDevice=null;
        HashMap<String, UsbDevice> devices = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = devices.values().iterator();
        int count = devices.size();
        if(count > 0) {
            while (deviceIterator.hasNext()) {
                UsbDevice device = deviceIterator.next();
                String devicename = device.getDeviceName();
                if(checkUsbDevicePidVid(device)){
                    Log.e("devicename",devicename);
                    myUsbDevice=device;
                }
            }
        }
    }
    // 寻找设备接口
    private boolean getDeviceInterface() {
        if (myUsbDevice != null) {
            for (int i = 0; i < myUsbDevice.getInterfaceCount(); i++) {
                UsbInterface intf = myUsbDevice.getInterface(i);
                if (i == 0) {
                    Interface1 = intf; // 保存设备接口
                    Log.e("成功获得设备接口:","" + Interface1.getId());
                }
                if (i == 1) {
                    Interface2 = intf;
                    Log.e("成功获得设备接口:","" + Interface2.getId());
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
        if (mInterface != null) {
            UsbDeviceConnection conn = null;
            // 在open前判断是否有连接权限；对于连接权限可以静态分配，也可以动态分配权限
            if (!manager.hasPermission(myUsbDevice)) {
                try {
                    pendingIntent.send(USBService.this,101,
                            new Intent());
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
                manager.requestPermission(myUsbDevice,pendingIntent);
            }
            if (manager.hasPermission(myUsbDevice)) {
                conn = manager.openDevice(myUsbDevice);
            }

            if (conn == null) {
                return;
            }
            if (conn.claimInterface(mInterface, true)) {
                myDeviceConnection = conn;
                if (myDeviceConnection != null)//
                    Log.e("open","设备成功！");
                final String mySerial = myDeviceConnection.getSerial();
            } else {
                conn.close();
            }
        }
    }
    public class MyBinder extends Binder {
        public void startDownload() {
            enumeraterDevices();
            //3)查找设备接口
            if (getDeviceInterface()) {
                //4)获取设备endpoint
                if (Interface1 != null) {
                    assignEndpoint(Interface1);
                    openDevice(Interface1);
                }
            }
        }

        // 发送数据
        public int sendMessageToPoint(byte[] buffer) {
            if (myDeviceConnection == null || epBulkOut == null) {
                return -1;
            } else {
                int size = myDeviceConnection.bulkTransfer(epBulkOut, buffer, buffer.length, 0);
                if (size < 0) {
                    return -1;
                } else {
                    return receiveMessageFromPoint();
                }
            }
        }

        //接收数据
        private int receiveMessageFromPoint() {
            byte[] buffer = new byte[64];
            int size = myDeviceConnection.bulkTransfer(epBulkIn, buffer, buffer.length, 2000);
            if (size < 0) {
                //没有
                return -2;
            } else {
                String str = new String(buffer, 0, size).trim();
                return 1;
            }
        }
    }

}
