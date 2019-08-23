package com.xuanyuan.library.help;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * 作者：罗发新
 * 时间：2019/4/24 0024    星期三
 * 邮件：424533553@qq.com
 * 说明： 获取各种硬件信息
 */
public class HardwareHelper {
    private final Context context;


    public HardwareHelper(Context context) {
        this.context = context;
    }


    /**
     * 返回转态
     * BatteryManager.BATTERY_PLUGGED_AC 1  有线充电状态
     * BatteryManager.BATTERY_PLUGGED_USB  2  USB 接通状态
     * BatteryManager.BATTERY_PLUGGED_WIRELESS  4 无线充电状态
     */
    public int getChargeState() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, filter);
        int chargePlug;
        if (batteryStatus != null) {
            chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            return chargePlug;
        }
        return -1;
    }

}
