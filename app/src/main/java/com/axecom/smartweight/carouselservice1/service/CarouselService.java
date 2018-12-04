package com.axecom.smartweight.carouselservice1.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.hardware.display.DisplayManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.WindowManager;

import com.axecom.smartweight.R;
import com.shangtongyin.tools.serialport.IConstants_ST;

import java.util.Objects;

/**
 * 双屏 副屏显示 功能
 * Created by Administrator on 2018/7/25.
 */

public class CarouselService extends Service implements IConstants_ST {
    private SecondScreen banner = null;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        //动态接受网络变化的广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.axecom.iweight.carouselservice.datachange");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    //自定义接受网络变化的广播接收器
    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (banner == null) {
                startBanner();
            }
            banner.questImage();
        }
    }

    public void startBanner() {
        DisplayManager displayManager = (DisplayManager) getApplicationContext().getSystemService(Context.DISPLAY_SERVICE);
        //获取屏幕数量
        Display[] presentationDisplays = displayManager.getDisplays();
        if (presentationDisplays.length > 1) {
            if (banner == null) {
                banner = new SecondScreen(getApplicationContext(), presentationDisplays[1]);
                Objects.requireNonNull(banner.getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
        }
        banner.show();
    }


    @Override
    public void onDestroy() {
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
        banner.cancel();
        banner = null;
        super.onDestroy();
        restartService();
        loop = false;
        unregisterReceiver(networkChangeReceiver);

    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    /**
     * 当轮播 服务关闭之后，自动重启服务
     */
    public void restartService() {
        Intent intent = new Intent(ACTION_START);
        sendBroadcast(intent);
    }

    //    START_STICKY：如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。
//    随后系统会尝试重新创建service，由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。
//    如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null。
//    START_NOT_STICKY：“非粘性的”。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统不会自动重启该服务。
//    START_REDELIVER_INTENT：重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，
//    系统会自动重启该服务，并将Intent的值传入。
//    START_STICKY_COMPATIBILITY：START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        flags = START_REDELIVER_INTENT;  //这句话将服务变成了 前台服务（四种方式） ，需要在销毁时一定调用   stopForeground(true);
        setForeground(intent);
        loop = true;
        startBanner();
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 设置成后台  服务
     *
     * @param intent 返回意图
     */
    private void setForeground(Intent intent) {
        if (intent != null) {
            Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
            // 设置PendingIntent
            builder.setContentIntent(PendingIntent.getService(this, 0, intent, 0)).
                    setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标).setContentTitle("下拉列表中的Title")// 设置下拉列表里的标题
                    .setSmallIcon(R.mipmap.ic_launcher)// 设置状态栏内的小图标
//                    .setContentText("要显示的内容")// 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = builder.build(); // 获取构建好的Notification
            notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音

            //让该service前台运行，避免手机休眠时系统自动杀掉该服务
            //如果 id 为 0 ，那么状态栏的 notification 将不会显示。
            //对于通过startForeground启动的service，onDestory方法中需要通过stopForeground(true)来取消前台运行状态
            startForeground(0, notification);
        }
    }

    private static boolean loop = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

//    Display id 0: DisplayInfo{"内置屏幕", uniqueId "local:0", app 1920 x 1008, real 1920 x 1080, largest app 1920 x 1810, smallest app 1080 x 970, 50.105 fps, supportedRefreshRates [50.105], rotation 0, density 240 (159.89508 x 160.42105) dpi, layerStack 0, appVsyncOff 0, presDeadline 20958088, type BUILT_IN, state ON, FLAG_SECURE, FLAG_SUPPORTS_PROTECTED_BUFFERS}, DisplayMetrics{density=1.5, width=1920, height=1008, scaledDensity=1.5, xdpi=159.89508, ydpi=160.42105}, isValid=true
//    Display id 1: DisplayInfo{"HDMI 屏幕", uniqueId "local:1", app 1024 x 600, real 1024 x 600, largest app 1024 x 600, smallest app 1024 x 600, 161316.34 fps, supportedRefreshRates [161316.34], rotation 0, density 177 (177.0 x 177.0) dpi, layerStack 1, appVsyncOff 0, presDeadline 1006199, type HDMI, state ON, FLAG_SECURE, FLAG_SUPPORTS_PROTECTED_BUFFERS, FLAG_PRESENTATION}, DisplayMetrics{density=1.10625, width=1024, height=600, scaledDensity=1.10625, xdpi=177.0, ydpi=177.0}, isValid=true