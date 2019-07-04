//package com.axecom.smartweight.manager;
//
//import android.content.Context;
//
//import com.inuker.bluetooth.library.BluetoothClient;
//
///**
// * Created by dingjikerbo on 2016/8/27.
// */
//public class ClientManager {
//
//    private static BluetoothClient mClient;
//
//    public static BluetoothClient getClient(Context context) {
//        if (mClient == null) {
//            synchronized (ClientManager.class) {
//                if (mClient == null) {
//                    mClient = new BluetoothClient(context);
//                }
//            }
//        }
//        return mClient;
//    }
//}
