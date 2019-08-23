package com.xuanyuan.library.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;



/**
 * 作者：罗发新
 * 时间：2019/4/25 0025    星期四
 * 邮件：424533553@qq.com
 * 说明：
 */
public class PollingService extends Service {
    public static final String ACTION = "com.ryantang.service.PollingService";

    private Notification mNotification;
    private NotificationManager mManager;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {

    }


    /**
     * Polling thread
     * 模拟向Server轮询的异步线程
     */
    int count = 0;
    class PollingThread extends Thread {
        @Override
        public void run() {
            System.out.println("Polling...");
            count ++;
            //当计数能被5整除时弹出通知
            if (count % 5 == 0) {

                System.out.println("New message!");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service:onDestroy");
    }
}