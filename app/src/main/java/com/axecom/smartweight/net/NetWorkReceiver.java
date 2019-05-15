package com.axecom.smartweight.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.axecom.smartweight.my.config.IConstants;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.luofx.utils.net.NetWorkJudge;

/**
 * Created by Administrator on 2018/7/19.
 */

public class NetWorkReceiver extends BroadcastReceiver implements IConstants {
    @Override
    public void onReceive(final Context context, Intent intent) {
        if ( ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            /*判断当前网络时候可用以及网络类型*/
            if (NetWorkJudge.isNetworkAvailable(context)) {
                LiveEventBus.get().with(EVENT_BUS_COMMON, String.class).post(EVENT_NET_WORK_AVAILABLE);
//                EventBus.getDefault().post(new BusEvent(BusEvent.NET_WORK_AVAILABLE, true));
            }
        }
    }

}
