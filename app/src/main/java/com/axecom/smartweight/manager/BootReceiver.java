package com.axecom.smartweight.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.axecom.smartweight.ui.activity.HomeActivity;
import com.axecom.smartweight.utils.LogUtils;

public class BootReceiver extends BroadcastReceiver {

    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.i("启动完成");
        if (ACTION_BOOT.equals(intent.getAction())){
            Intent mBootIntent = new Intent(context, HomeActivity.class);
            mBootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mBootIntent);
        }
   /*     if(intent.getAction().equals("com.axecom.destroy")){
            Intent sevice = new Intent(context, BannerService.class);
            context.startService(sevice);
        }*/
    }
}
