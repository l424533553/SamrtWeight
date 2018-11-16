package com.axecom.smartweight.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.axecom.smartweight.ui.activity.HomeActivity;
import com.shangtongyin.tools.serialport.IConstants_ST;


public class BootReceiver extends BroadcastReceiver implements IConstants_ST {

    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    private Intent intentService;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case ACTION_BOOT:
                    Intent mBootIntent = new Intent(context, HomeActivity.class);
                    mBootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(mBootIntent);
                    mBootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//                    intentService = new Intent(context.getApplicationContext(), CarouselService.class);
//                    context.startService(intentService);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        //版本必须大于5.0
//                        context.getApplicationContext().startService(new Intent(context.getApplicationContext(), JobWakeUpService.class));
//                    }


                    break;
                case ACTION_START:
                    if (intentService != null) {
                        context.startService(intentService);
                    }
                    break;
                case ACTION_DESTORY:
                    if (intentService != null) {
                        context.stopService(intentService);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}