package com.luofx.utils.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * 网络连接 判断
 * Created by user on 2016/5/11.
 */
public class NetWorkJudge {

    /**
     * 判断 网络是否 可用
     */
    public static boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (NetworkInfo networkInfo1 : networkInfo) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo1.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 该处为R02 安鑫宝独写的网络状态检测 工具方法
     */
    public static boolean isWifiAvailable(Context context){
        //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            //步骤1：通过Context.getSystemService(Context.CONNECTIVITY_SERVICE)获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //步骤2：获取ConnectivityManager对象对应的NetworkInfo对象
            //NetworkInfo对象包含网络连接的所有信息
            //步骤3：根据需要取出网络连接信息
            //获取WIFI连接的信息
            NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
//            NetworkInfo  networkInfo2 = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//            Boolean isMobileConn = networkInfo2.isConnected();

            return networkInfo.isConnected();
//            MyLog.myInfo("网络改变了:" + isWifiConn);
//            MyToast.toastShort(context.getApplicationContext(), "网络改变了:" + isWifiConn);

        } else { //API大于23时使用下面的方式进行网络监听

            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();
            //用于存放网络连接信息
            StringBuilder sb = new StringBuilder();
            //通过循环将网络信息逐个取出来
            for (Network network : networks) {
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
                sb.append(networkInfo.getTypeName()).append(" connect is ").append(networkInfo.isConnected());
            }
        }
        return false;
    }


    /**
     * 没有网络
     */
    public static final int NONETWORK = 0;
    /**
     * 当前是wifi连接
     */
    public static final int WIFI = 1;
    /**
     * 不是wifi连接
     */
    public static final int NOWIFI = 2;


    /**
     * 检测当前网络的类型 是否是wifi
     */
    public static int checkedNetWorkType(Context context){
        if(isNetWorkError(context)){
            return NONETWORK;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting() ){
            return WIFI;
        }else{
            return NOWIFI;
        }
    }


    /**
     * 检查是否连接网络

     */
    public static boolean isNetWorkError(Context context){
        // 1.获得连接设备管理器
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm!=null){
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo == null || !networkInfo.isAvailable();
        }else {
            return true;
        }
    }

}
