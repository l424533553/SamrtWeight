package com.xuanyuan.library.utils.system;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.xuanyuan.library.entity.DeviceInfoDao;
import com.xuanyuan.library.entity.Deviceinfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/**
 * 作者：罗发新
 * 时间：2019/6/5 0005    星期三
 * 邮件：424533553@qq.com
 * 说明：用于获取系统信息 的工具类
 */
public class SystemInfoUtils {

    /**
     * @return 返回系统的 网络mac 地址， "" 或不可用
     */
    @SuppressLint("HardwareIds")
    public static String getMac(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            return getmac6();
        } else {
            String mac = "";
            if (context == null) {
                return mac;
            }
            WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            if (wm != null) {
                WifiInfo wifiInfo = wm.getConnectionInfo();
                if (wifiInfo != null) {
                    mac = wifiInfo.getMacAddress();
                }
            }
            return mac;
        }
    }


    /**
     * 6.0以后的技术
     *
     * @return mac地址
     */
    public static String getmac6() {
        try {
            for (Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces(); networkInterfaces.hasMoreElements(); ) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if ("wlan0".equals(networkInterface.getName())) {
                    byte[] hardwareAddress = networkInterface.getHardwareAddress();
                    if (hardwareAddress == null || hardwareAddress.length == 0) {
                        continue;
                    }
                    StringBuilder buf = new StringBuilder();
                    for (byte b : hardwareAddress) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    return buf.toString().toLowerCase();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @return 根据串口名获取  USB设备
     */
    public static UsbDevice getUsbDeviceFromName(Context context, String usbName) {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> usbDeviceList = usbManager.getDeviceList();
        return usbDeviceList.get(usbName);
    }


    /**
     * @return 获取包信息的Code
     */
    public static long getVersionCode(Context context) {
        PackageInfo pi = getPackageInfo(context);
        if (pi == null) {
            return 1;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return pi.getLongVersionCode();
            } else {
                return pi.versionCode;
            }
        }
    }

    /**
     * @return 获取包信息的版本名
     */
    public static String getVersionName(Context context) {
        PackageInfo pi = getPackageInfo(context);
        if (pi == null) {
            return "1.0";
        } else {
            return pi.versionName;
        }
    }

    /**
     * @return 包信息
     */
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取硬件信息  各项参数
     */
    public static void getHardwareInfo(Context context) {
        TelephonyManager phone = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        @SuppressLint("HardwareIds")
        String mac = wifi.getConnectionInfo().getMacAddress();   // mac 地址
        int phonetype = phone.getPhoneType();  //  手机类型
        String model = android.os.Build.MODEL; // ****  手机型号
        String sdk = String.valueOf(Build.VERSION.SDK_INT); // 系统版本值

        String brand = android.os.Build.BRAND;
        String release = phone.getDeviceSoftwareVersion();// 系统版本 ,
        int networktype = phone.getNetworkType();   // 网络类型
        String networkoperatorname = phone.getNetworkOperatorName();   // 网络类型名
        String radioVersion = android.os.Build.getRadioVersion();   // 固件版本

        // 设备信息
        Deviceinfo deviceinfo = new Deviceinfo(release, sdk, brand, model, networkoperatorname, networktype, phonetype, mac, radioVersion);
        DeviceInfoDao deviceInfoDao = new DeviceInfoDao(context);
        deviceInfoDao.insert(deviceinfo);
    }

    //    安装应用界面
    public static void install(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    //    判断微信是否安装
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }
}
