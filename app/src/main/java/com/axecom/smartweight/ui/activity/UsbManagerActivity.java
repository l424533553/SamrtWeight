//package com.axecom.iweight.ui.activity;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.hardware.usb.UsbDevice;
//import android.hardware.usb.UsbManager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.TextView;
//
//import com.axecom.iweight.R;
//import com.axecom.iweight.base.BaseActivity;
//import com.axecom.iweight.manager.TemperatureUsbControl;
//import com.axecom.iweight.utils.LogUtils;
//
//public class UsbManagerActivity extends BaseActivity {
//    private View rootView;
//    private TextView msgTv;
//    TemperatureUsbControl mTemperatureUsbControl;
//
//    @Override
//    public View setInitView() {
//        rootView = LayoutInflater.from(this).inflate(R.layout.usb_manager_activity, null);
//        msgTv = rootView.findViewById(R.id.usb_manager_msg_tv);
//        initUsbControl();
//        return rootView;
//    }
//
//    @Override
//    public void onClick(View v) {
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
//        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
//        registerReceiver(usbReceiver, intentFilter);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
////        mTemperatureUsbControl.onPause();
//        unregisterReceiver(usbReceiver);
//    }
//
//    /**
//     * 初始化USB
//     */
//    private void initUsbControl() {
//        mTemperatureUsbControl = new TemperatureUsbControl(this);
//        mTemperatureUsbControl.initUsbControl();
//    }
//
//    private BroadcastReceiver usbReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
//                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//                mTemperatureUsbControl.onPause();
//                if (device != null) {
//                    if (device.getVendorId() == 6790 && device.getProductId() == 29987) {
//                        showLoading("读卡器被拔出，请检查设备");
//                    }
//                    if (device.getVendorId() == 26728 && device.getProductId() == 1280) {
//                        showLoading("打印机被拔出，请检查设备");
//                    }
//                }
//            }
//            if (intent.getAction().equals("android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
//                mTemperatureUsbControl.initUsbControl();
//                mTemperatureUsbControl.onDeviceStateChange();
//                LogUtils.d("ACTION_USB_DEVICE_ATTACHED");
//
//            }
//        }
//    };
//}
