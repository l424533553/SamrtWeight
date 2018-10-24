package com.axecom.smartweight.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.axecom.smartweight.base.BusEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/7/19.
 */

public class NetWorkReceiver extends BroadcastReceiver {

    private static final String ACTION_NET_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_NET_CHANGE)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                EventBus.getDefault().post(new BusEvent(BusEvent.NET_WORK_AVAILABLE, true));
            } else {
            }
        }
    }

}
