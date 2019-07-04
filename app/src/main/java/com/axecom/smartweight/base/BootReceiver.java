package com.axecom.smartweight.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.axecom.smartweight.activity.common.mvvm.home.HomeActivity;
import com.axecom.smartweight.config.IConstants;
import com.xuanyuan.library.utils.LiveBus;
import com.xuanyuan.library.utils.net.MyNetWorkUtils;

import static com.xuanyuan.library.config.IConfig.EVENT_BUS_COMMON;

public class BootReceiver extends BroadcastReceiver implements IConstants {

    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    // 计量院不定时token失效。
    private static final String ACTION_TOKEN_REGET = "com.axecom.smartweight.manager.REGET";
    private Intent intentService;

    public BootReceiver(Intent intentService) {
        this.intentService = intentService;
    }

    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case ACTION_TOKEN_REGET:

                    LiveBus.post(EVENT_BUS_COMMON, String.class, EVENT_TOKEN_REGET);
                    break;
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    /*判断当前网络时候可用以及网络类型*/
                    if (MyNetWorkUtils.isNetworkAvailable(context)) {
                        LiveBus.post(EVENT_BUS_COMMON, String.class, EVENT_NET_WORK_AVAILABLE);
                    }
                    break;
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