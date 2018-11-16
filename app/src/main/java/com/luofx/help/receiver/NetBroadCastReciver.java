package com.luofx.help.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;


/**
 * 说明：
 * 作者：User_luo on 2018/7/16 11:23
 * 邮箱：424533553@qq.com
 */
public class NetBroadCastReciver extends BroadcastReceiver {

    /**
     * 只有当网络改变的时候才会 经过广播。
     */

    @Override
    public void onReceive(Context context, Intent intent) {

//        boolean isNetAble = netWorkJudge(context, intent);
//        MyApplication myApplication = (MyApplication) context.getApplicationContext();
//        if (isNetAble) {// 网络
//            myApplication.setNetAble(true);
//        } else {
//            myApplication.setNetAble(false);
//        }

        //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            checkState_23(context);

        } else { //API大于23时使用下面的方式进行网络监听
            checkState_23orNew(context);
        }


    }

    private void netWifiJudge(Intent intent) {
        //判断wifi是打开还是关闭
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) { //此处无实际作用，只是看开关是否开启
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    break;

                case WifiManager.WIFI_STATE_DISABLING:
                    break;
                case WifiManager.WIFI_STATE_ENABLED: //wifi 可用
                    break;
            }
        }
    }

    private boolean netWorkJudge(Context context, Intent intent) {
        //此处是主要代码，
        //如果是在开启wifi连接和有网络状态下
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                // 获取NetworkInfo对象
                NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

                if (networkInfo != null && networkInfo.length > 0) {
                    for (int i = 0; i < networkInfo.length; i++) {
                        // 判断当前网络状态是否为连接状态
                        if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    /**
     * 网络判断
     */
    private boolean netWorkJudge(Intent intent) {
        //如果是在开启wifi连接和有网络状态下
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (NetworkInfo.State.CONNECTED == info.getState()) {
                //连接状态
                Log.e("pzf", "有网络连接");
                return true;
                //执行后续代码
                //new AutoRegisterAndLogin().execute((String)null);
                //ps:由于boradCastReciver触发器组件，他和Service服务一样，都是在主线程的，所以，如果你的后续操作是耗时的操作，请new Thread获得AsyncTask等，进行异步操作
            } else {
                Log.e("pzf", "无网络连接");
            }
        }
        return false;
    }


    //检测当前的网络状态
    //API版本23以下时调用此方法进行检测,因为API23后getNetworkInfo(int networkType)方法被弃用
    public void checkState_23(Context context) {
        //步骤1：通过Context.getSystemService(Context.CONNECTIVITY_SERVICE)获得ConnectivityManager对象
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //步骤2：获取ConnectivityManager对象对应的NetworkInfo对象
        //NetworkInfo对象包含网络连接的所有信息
        //步骤3：根据需要取出网络连接信息
        //获取WIFI连接的信息
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Boolean isWifiConn = networkInfo.isConnected();
//        MyLog.myInfo("网络改变了:" + isWifiConn);
//        MyToast.toastShort(context.getApplicationContext(), "网络改变了:" + isWifiConn);

        //获取移动数据连接的信息
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        Boolean isMobileConn = networkInfo.isConnected();
    }

    // API 23及以上时调用此方法进行网络的检测
// getAllNetworks() 在API 21后开始使用
//步骤非常类似
    public void checkState_23orNew(Context context) {
        //获得ConnectivityManager对象
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //获取所有网络连接的信息
        Network[] networks = connMgr.getAllNetworks();
        //用于存放网络连接信息
        StringBuilder sb = new StringBuilder();
        //通过循环将网络信息逐个取出来
        for (int i = 0; i < networks.length; i++) {
            //获取ConnectivityManager对象对应的NetworkInfo对象
            NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
            sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
        }
    }


}
