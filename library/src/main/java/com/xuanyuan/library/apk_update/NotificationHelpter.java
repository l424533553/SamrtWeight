package com.xuanyuan.library.apk_update;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.xuanyuan.library.R;

import static android.app.Notification.VISIBILITY_SECRET;

/**
 * 作者：罗发新
 * 时间：2018/12/14 0014    14:04
 * 邮件：424533553@qq.com
 * 说明： 消息通知栏的创建   ，注意 26及以后的创建特殊性
 */
public class NotificationHelpter {

    private final Context context;

    public NotificationHelpter(Context context) {
        this.context = context;
    }

    /**
     * 只能用于低于 sdk26的版本
     * 创建一个消息通知栏
     */
    public void createNotification(@NonNull Context context, @NonNull String channelId) {
        // 创建通知栏管理工具
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        //  实例化通知栏构造器
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId);
        //设置标题
        mBuilder.setContentTitle("我是标题")
                //设置内容
                .setContentText("我是内容")
                //设置大图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                //设置小图标
                .setSmallIcon(R.mipmap.ic_launcher_round)
                //设置通知时间
                .setWhen(System.currentTimeMillis())
                //首次进入时显示效果
                .setTicker("我是测试内容")
                //设置通知方式，声音，震动，呼吸灯等效果，这里通知方式为声音
                .setDefaults(Notification.DEFAULT_SOUND);
        //发送通知请求
        notificationManager.notify(10, mBuilder.build());
    }


    /**
     * 兼容版
     * 消息通知的 等级说明
     * 高 ：发出声音并出现在屏幕上，用户必须立即知道或采取行动的时间关键信息， 例如：短信，闹钟，电话
     * 默认：发出声音   应该在用户最早的方便时看到的信息，但不要打断他们正在做的事情  例如：交通警报，任务提醒
     * 低：没有声音   通知渠道不符合其他重要性级别的要求  例如：用户订阅的新内容，社交网络邀请
     * MIN：没有声音或视觉中断    可以等待或与用户无关的非必要信息     例如：附近的景点，天气，促销内容
     * 没有：不要在阴凉处显示     通常，按用户请求抑制来自包的通知     例如：被阻止的应用通知
     *
     * @param remoteViews 布局控制文件
     * @param downloadId  下载id
     */
    public  Notification getNotification(NotificationManager mNotifyManager, @NonNull RemoteViews remoteViews, int downloadId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 需要设置 channelId(随意取)  channelName(随意取)
            //TODO  等级信息
            //消息登记    public static final int IMPORTANCE_DEFAULT = 3;  importance 消息等级
            //    public static final int IMPORTANCE_HIGH = 4;
            //    public static final int IMPORTANCE_LOW = 2;
            //    public static final int IMPORTANCE_MAX = 5;
            //    public static final int IMPORTANCE_MIN = 1;
            //    public static final int IMPORTANCE_NONE = 0;
            NotificationChannel channel = new NotificationChannel("channel_id", "channel_name",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.canBypassDnd();//是否绕过请勿打扰模式
            channel.enableLights(true);//闪光灯
            channel.setLockscreenVisibility(VISIBILITY_SECRET);//锁屏显示通知
            channel.setLightColor(Color.RED);//闪关灯的灯光颜色
            channel.canShowBadge();//桌面launcher的消息角标
            channel.enableVibration(true);//是否允许震动
            channel.getAudioAttributes();//获取系统通知响铃声音的配置
            channel.getGroup();//获取通知取到组
            channel.setBypassDnd(true);//设置可绕过  请勿打扰模式
            channel.setVibrationPattern(new long[]{100, 100, 200});//设置震动模式
            channel.shouldShowLights();//是否会有灯光
            mNotifyManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id");

        builder.setContentTitle("新消息来了");// 消息标题
        builder.setContentText("周末到了，不用上班了");
        builder.setSmallIcon(R.mipmap.ic_launcher);// 通知的小图标
        builder.setAutoCancel(true)
                .setOngoing(false)//设置为ture，表示它为一个正在进行的通知。简单的说，当为ture时，不可以被侧滑消失。
//                .setDeleteIntent(new PendingIntent()) //清除通知栏时的相应
                .setTicker("正在下载") // 下面的小标题
//                .setContent("") // 下面的小标题
//                .setContentIntent()// 消息主题被点击时做出对应的响应
                .setWhen(System.currentTimeMillis())// 设置通知时间，此事件用于通知栏排序
                .setCustomContentView(remoteViews);//通知栏的布局界面 ，sdk 25以下找不到该方法

        Notification mNotification = builder.build();// 消息通知
        mNotifyManager.notify(downloadId, mNotification);
        return mNotification;
    }

}
