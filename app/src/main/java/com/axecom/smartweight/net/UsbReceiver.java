package com.axecom.smartweight.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2018/7/5.
 */

public class UsbReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
//            UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//            if (device != null) {
//                if (device.getVendorId() == 6790 && device.getProductId() == 29987) {
//                    ((BaseActivity)context).showLoading("读卡器被拔出，请检查设备");
//                }
//                if (device.getVendorId() == 26728 && device.getProductId() == 1280) {
//                    ((BaseActivity)context).showLoading("打印机被拔出，请检查设备");
//                }
//            }
//        }
    }
}
